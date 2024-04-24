import React, { useState } from 'react';
import { Container, TextInput, Button, Title, Text } from '@mantine/core';
import { useHistory } from 'react-router-dom';
import { HeaderMegaMenu } from '@/components/HeaderMegaMenu/HeaderMegaMenu';
import classes from "@/components/HeaderMegaMenu/HeaderMegaMenu.module.css";

const Signup = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');

    const handleSignup = async () => {
        try {
            const response = await fetch('http://localhost:8080/users/signin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username,
                    password,
                    email,
                }),
            });

            if (response.ok) {
                // If signup is successful, do something (e.g., redirect)
                const data = await response.json();
                console.log('Signed up user:', data);
            } else {
                const errorMessage = await response.text();
                setError(errorMessage);
            }
        } catch (error) {
            console.error('Error signing up:', error);
            setError('An error occurred while signing up');
        }
    };

    return (
        <>
            <Title ta="center" mt={100}>
                Signup
            </Title>
            <HeaderMegaMenu />
            <Container style={{ textAlign: 'center', marginTop: '50px' }}>
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
                  placeholder="Enter your password (minimum 7 characters)"
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                  style={{ marginBottom: '20px' }}
                />
                <Text size="sm" color="dimmed" style={{ marginBottom: '10px' }}>
                    Password must be at least 7 characters long and contain at least one uppercase character,
                    and one special character(_!#$%&'*+/=?`~^.-)
                </Text>
                <TextInput
                  label="Email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  style={{ marginBottom: '20px' }}
                />
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <Button onClick={handleSignup}>Signup</Button>
                    <a href="/Welcome" className={classes.link}> </a>
            </Container>
        </>
    );
};

export default Signup;
