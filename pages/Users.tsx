import React, { useState, useEffect } from 'react';
import { Title, Text, Anchor, Container, Button, TextInput, Badge } from '@mantine/core';
import classes from './Test.module.css';
import { HeaderMegaMenu } from '@/components/HeaderMegaMenu/HeaderMegaMenu';
import { ThumbsupIcon, ThumbsdownIcon } from '@primer/octicons-react'; // Import icons from Octicons
import { NavbarNested } from '../components/NavbarNested/NavbarNested';
import {ThumbsUpIcon} from "@storybook/icons";

export function Questions() {
  const [users, setUser] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await fetch('http://localhost:8080/users/getAll');
        if (!response.ok) throw new Error('Failed to fetch');
        const data = await response.json();
        setUser(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);


  if (loading) return <p>Loading questions...</p>;
  if (error) return <p>Error loading questions: {error}</p>;

  return (
      <>
        <Title className={classes.title} ta="center" mt={100}>
          Stack
          <Text inherit variant="gradient" component="span" gradient={{ from: 'pink', to: 'yellow' }}>
            Underflow
          </Text>
        </Title>
        <HeaderMegaMenu />
        <Container size="sm" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <table style={{ width: '100%', textAlign: 'center' }}>
            <tbody>
            {users.map((user) => (
                <tr key={user.id}>
                  <td style={{ textAlign: 'left', paddingBottom: '20px' }}>
                    <div>
                      <Text size="lg" weight="bold">{user.username}</Text>
                      <Text size="sm" color="purple">Contact: {user.email}</Text>
                      <Text size="sm" color="blue">User score: {user.score}</Text>
                      <Text size="sm" color="green">{user.tags}</Text>
                    </div>
                  </td>
                  <td>
                    <Text size="sm">{user.creationDateTime}</Text>
                  </td>
                </tr>
            ))}
            </tbody>
          </table>
        </Container>
      </>
  );
}


export default Questions;
