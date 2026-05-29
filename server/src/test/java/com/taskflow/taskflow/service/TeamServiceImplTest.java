package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.TeamMemberRepository;
import com.taskflow.taskflow.dao.TeamRepository;
import com.taskflow.taskflow.dao.UserRepository;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.TeamMember;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import com.taskflow.taskflow.dto.team.UpdateTeamRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
//import org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private TeamAccessService teamAccessService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TeamServiceImpl teamService;

    private static Team sampleTeam;
    private static User sampleUser;

    // Setup User and Team fixtures for tests
    @BeforeAll
    static void beforeAll() {
        sampleUser = new User();
        sampleUser.setId(1);

        sampleTeam = new Team();
        sampleTeam.setId(1);
        sampleTeam.setName("Engineering");
        sampleTeam.setDescription("Engineering team");
        sampleTeam.setCreatedBy(sampleUser);
    }

    @BeforeEach
    void setUp() {
        // reset mocks behavior if needed between tests
        Mockito.reset(teamRepository, userRepository, teamMemberRepository, teamAccessService, authService);
    }

    @AfterEach
    void afterEach() {
        // no-op but place for tear-down if needed
    }

    @AfterAll
    static void afterAll() {
        // cleanup static fixtures if needed
        sampleTeam = null;
        sampleUser = null;
    }

    @Test
    void shouldReturnTeamWhenFoundById() {
        Team expectedTeam = new Team();
        expectedTeam.setId(1);
        expectedTeam.setName("Engineering");
        expectedTeam.setDescription("Engineering team");

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(expectedTeam));

        Team actualTeam = teamService.findById(1);

        Assertions.assertEquals(expectedTeam, actualTeam);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenTeamDoesNotExist() {
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> teamService.findById(1));
    }

    @Test
    void addTeamShouldAddTeamSuccessfully() {
        // Arrange
        User creator = new User();
        creator.setId(1);

        Team team = new Team();
        team.setName("Test Team");

        Mockito.when(userRepository.findById(1))
                .thenReturn(Optional.of(creator));

        Mockito.when(teamRepository.existsByNameAndCreatedBy("Test Team", creator))
                .thenReturn(false);

        Mockito.when(teamRepository.save(team)).thenReturn(team);

        Team addedTeam = teamService.save(team, 1);
        // Assert
        Assertions.assertNotNull(addedTeam);
        Assertions.assertEquals(creator, addedTeam.getCreatedBy());
        Assertions.assertEquals(team.getId(), addedTeam.getId());

        Mockito.verify(teamRepository).save(team);
        Mockito.verify(teamMemberRepository).save(Mockito.any(TeamMember.class));
    }

    @Test
    void updateTeamShouldModifyAndSaveTeam() {
        // Arrange
        UpdateTeamRequest req = new UpdateTeamRequest();
        req.setName("New Name");
        req.setDescription("A longer description to satisfy validation");

        Mockito.when(authService.getCurrentUser()).thenReturn(sampleUser);
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(sampleTeam));
        // make validateOwnerAccess a no-op
        Mockito.doNothing().when(teamAccessService).validateOwnerAccess(1, sampleUser.getId());
        Mockito.when(teamRepository.save(Mockito.any(Team.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Team updated = teamService.updateTeam(1, req);

        // Assert
        Assertions.assertEquals("New Name", updated.getName());
        Assertions.assertEquals("A longer description to satisfy validation", updated.getDescription());
        Mockito.verify(teamRepository).save(updated);
    }

    @Test
    void deleteByIdShouldDeleteWhenExists() {
        // Arrange
        Mockito.when(authService.getCurrentUser()).thenReturn(sampleUser);
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(sampleTeam));
        Mockito.doNothing().when(teamAccessService).validateOwnerAccess(1, sampleUser.getId());
        // explicitly use doNothing for the void delete
        Mockito.doNothing().when(teamRepository).delete(sampleTeam);

        // Act
        teamService.deleteById(1);

        // Assert
        Mockito.verify(teamRepository).delete(sampleTeam);
    }

    @Test
    void deleteByIdShouldThrowWhenTeamNotFound() {
        Mockito.when(authService.getCurrentUser()).thenReturn(sampleUser);
        Mockito.when(teamRepository.findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> teamService.deleteById(2));
    }

    @Test
    void runtimeExceptionFromRepositoryIsPropagated() {
        Mockito.when(teamRepository.findById(1)).thenThrow(new RuntimeException("DB down"));

        Assertions.assertThrows(RuntimeException.class, () -> teamService.findById(1));
    }
}
