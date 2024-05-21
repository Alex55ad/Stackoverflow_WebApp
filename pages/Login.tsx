import React, { useState } from 'react';
import { Container, TextInput, Button } from '@mantine/core';
import {Text, Title} from "@mantine/core/lib";
import { redirect } from 'react-router-dom';
import classes from "@/pages/Test.module.css";
import {HeaderMegaMenu} from "@/components/HeaderMegaMenu/HeaderMegaMenu";
import { useNavigate } from "react-router-dom";

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async () => {
        try {
            const response = await fetch(`http://localhost:8080/users/login?username=${username}&password=${password}`, {
                method: 'POST',
            });
            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('user', JSON.stringify(data));
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
            <a href="/Welcome" className={classes.link}> 
                    <Button onClick={handleLogin}>Login
                    </Button>
            </a>
        </Container>
    );
};

export default Login;
