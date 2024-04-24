import React, { useState } from 'react';
import { Container, TextInput, Button } from '@mantine/core';
import {Text, Title} from "@mantine/core/lib";
import classes from "@/pages/Test.module.css";
import {HeaderMegaMenu} from "@/components/HeaderMegaMenu/HeaderMegaMenu";

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async () => {
        try {
            const response = await fetch('http://localhost:8080/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username,
                    password: password,
                }),
            });

            if (response.ok) {
                // If login is successful, do something (e.g., redirect)
                const data = await response.json();
                console.log('Logged in user:', data);
            } else {
                const errorMessage = await response.text();
                setError(errorMessage);
            }
        } catch (error) {
            console.error('Error logging in:', error);
            setError('An error occurred while logging in');
        }
    };

    return (
        <Container style={{ textAlign: 'center', marginTop: '50px' }}>
            <h2>Login</h2>
            <TextInput
                label="Username"
                placeholder="Enter your username"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
                style={{ marginBottom: '20px' }}
            />
            <TextInput
                label="Password"
                type="password"
                placeholder="Enter your password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                style={{ marginBottom: '20px' }}
            />
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <Button onClick={handleLogin}>Login</Button>
        </Container>
    );
};

export default Login;
