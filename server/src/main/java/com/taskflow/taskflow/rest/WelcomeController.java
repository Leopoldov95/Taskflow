package com.taskflow.taskflow.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public ResponseEntity<String> welcome() {
        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>TaskFlow API</title>
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                        body {
                            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                            background: #0f172a;
                            color: #e2e8f0;
                            min-height: 100vh;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                        }
                        .container {
                            text-align: center;
                            padding: 2rem;
                        }
                        .badge {
                            display: inline-block;
                            background: #1e293b;
                            border: 1px solid #334155;
                            border-radius: 999px;
                            padding: 0.25rem 1rem;
                            font-size: 0.75rem;
                            color: #94a3b8;
                            margin-bottom: 1.5rem;
                            letter-spacing: 0.1em;
                            text-transform: uppercase;
                        }
                        h1 {
                            font-size: 3rem;
                            font-weight: 700;
                            background: linear-gradient(135deg, #6366f1, #8b5cf6);
                            -webkit-background-clip: text;
                            -webkit-text-fill-color: transparent;
                            margin-bottom: 1rem;
                        }
                        p {
                            color: #94a3b8;
                            font-size: 1.1rem;
                            margin-bottom: 2rem;
                            max-width: 400px;
                            margin-left: auto;
                            margin-right: auto;
                        }
                        .btn {
                            display: inline-flex;
                            align-items: center;
                            gap: 0.5rem;
                            background: linear-gradient(135deg, #6366f1, #8b5cf6);
                            color: white;
                            text-decoration: none;
                            padding: 0.75rem 1.75rem;
                            border-radius: 8px;
                            font-weight: 600;
                            font-size: 0.95rem;
                            transition: opacity 0.2s;
                        }
                        .btn:hover {
                            opacity: 0.85;
                        }
                        .version {
                            margin-top: 3rem;
                            color: #475569;
                            font-size: 0.8rem;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="badge">v1.0 · REST API</div>
                        <h1>TaskFlow API</h1>
                        <p>A task management REST API built with Spring Boot and MySQL.</p>
                        <a href="https://taskflow.leoortega.com/swagger-ui/index.html" class="btn">
                            View API Docs →
                        </a>
                        <div class="version">Built with Spring Boot · Deployed on netcup VPS</div>
                    </div>
                </body>
                </html>
                """;
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }
}