import React, { useState, useEffect } from 'react'
import { useForm, Controller } from 'react-hook-form'
import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { toast } from 'sonner'
import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import {
  Field,
  FieldError,
  FieldGroup,
  FieldLabel,
} from '@/components/ui/field'
// looks like `import type` is new?
import type { AuthMode, FormValues } from '@/types/auth'

interface AuthFormProps {
  mode: AuthMode
  onModeChange: (mode: AuthMode) => void
  onSubmit: (data: FormValues) => void
}

const signInSchema = z.object({
  email: z.email('Invalid email address'),
  password: z.string().min(4, 'Password must be at least 5 characters'),
})

const registerSchema = z
  .object({
    firstName: z.string().min(1, 'First name is required'),
    lastName: z.string().min(1, 'Last name is required'),
    email: z.email('Invalid email address'),
    password: z.string().min(4, 'Password must be at least 5 characters'),
    confirmPassword: z.string().min(4, 'Password confirmation is required'),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ['confirmPassword'],
  })

export default function AuthForm({
  mode,
  onModeChange,
  onSubmit,
}: AuthFormProps) {
  const schema = mode === 'signIn' ? signInSchema : registerSchema
  const defaultValues =
    mode === 'signIn'
      ? { email: '', password: '' }
      : {
          firstName: '',
          lastName: '',
          email: '',
          password: '',
          confirmPassword: '',
        }

  const form = useForm<FormValues>({
    resolver: zodResolver(schema),
    defaultValues,
  })

  useEffect(() => {
    form.reset(defaultValues)
  }, [mode, form])

  function handleSubmit(data: FormValues) {
    // ensure form data is valid before calling onSubmit
    if (!form.formState.isValid) {
      toast.error('Please correct the errors in the form')
      return
    }

    onSubmit(data)
  }

  return (
    <Card className="w-full sm:max-w-md">
      <CardHeader>
        <CardTitle className="text-center">
          {mode === 'signIn' ? 'Sign In' : 'Register'}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form id="form-rhf-demo" onSubmit={form.handleSubmit(handleSubmit)}>
          <FieldGroup>
            {mode === 'register' && (
              <>
                <Controller
                  name="firstName"
                  control={form.control}
                  render={({ field, fieldState }) => (
                    <Field data-invalid={fieldState.invalid}>
                      <FieldLabel htmlFor="firstName">First Name</FieldLabel>
                      <Input
                        {...field}
                        id="firstName"
                        aria-invalid={fieldState.invalid}
                        placeholder="Enter your first name"
                        autoComplete="given-name"
                      />
                      {fieldState.invalid && (
                        <FieldError errors={[fieldState.error]} />
                      )}
                    </Field>
                  )}
                />
                <Controller
                  name="lastName"
                  control={form.control}
                  render={({ field, fieldState }) => (
                    <Field data-invalid={fieldState.invalid}>
                      <FieldLabel htmlFor="lastName">Last Name</FieldLabel>
                      <Input
                        {...field}
                        id="lastName"
                        aria-invalid={fieldState.invalid}
                        placeholder="Enter your last name"
                        autoComplete="family-name"
                      />
                      {fieldState.invalid && (
                        <FieldError errors={[fieldState.error]} />
                      )}
                    </Field>
                  )}
                />
              </>
            )}
            <Controller
              name="email"
              control={form.control}
              render={({ field, fieldState }) => (
                <Field data-invalid={fieldState.invalid}>
                  <FieldLabel htmlFor="email">Email</FieldLabel>
                  <Input
                    {...field}
                    id="email"
                    aria-invalid={fieldState.invalid}
                    placeholder="Enter your email"
                    autoComplete="email"
                    type="email"
                  />
                  {fieldState.invalid && (
                    <FieldError errors={[fieldState.error]} />
                  )}
                </Field>
              )}
            />
            <Controller
              name="password"
              control={form.control}
              render={({ field, fieldState }) => (
                <Field data-invalid={fieldState.invalid}>
                  <FieldLabel htmlFor="password">Password</FieldLabel>
                  <Input
                    {...field}
                    id="password"
                    aria-invalid={fieldState.invalid}
                    placeholder="Enter your password"
                    autoComplete={
                      mode === 'signIn' ? 'current-password' : 'new-password'
                    }
                    type="password"
                  />
                  {fieldState.invalid && (
                    <FieldError errors={[fieldState.error]} />
                  )}
                </Field>
              )}
            />
            {mode === 'register' && (
              <Controller
                name="confirmPassword"
                control={form.control}
                render={({ field, fieldState }) => (
                  <Field data-invalid={fieldState.invalid}>
                    <FieldLabel htmlFor="confirmPassword">
                      Confirm Password
                    </FieldLabel>
                    <Input
                      {...field}
                      id="confirmPassword"
                      aria-invalid={fieldState.invalid}
                      placeholder="Confirm your password"
                      autoComplete="new-password"
                      type="password"
                    />
                    {fieldState.invalid && (
                      <FieldError errors={[fieldState.error]} />
                    )}
                  </Field>
                )}
              />
            )}
          </FieldGroup>
        </form>
      </CardContent>
      <CardFooter className="flex flex-col gap-2">
        <Button type="submit" form="form-rhf-demo" className="w-full">
          {mode === 'signIn' ? 'Sign In' : 'Register'}
        </Button>
        <p className="text-center text-sm">
          {mode === 'signIn'
            ? "Don't have an account? "
            : 'Already have an account? '}
          <button
            type="button"
            onClick={() =>
              onModeChange(mode === 'signIn' ? 'register' : 'signIn')
            }
            className="text-primary hover:underline cursor-pointer hover:text-primary/80 transition-colors"
          >
            {mode === 'signIn' ? 'Register' : 'Sign In'}
          </button>
        </p>
      </CardFooter>
    </Card>
  )
}
