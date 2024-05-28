import React, { useState, useEffect } from 'react';
import { Title, Text, Container, Button } from '@mantine/core';
import classes from './Test.module.css';
import { HeaderMegaMenu } from '@/components/HeaderMegaMenu/HeaderMegaMenu';

export function Users() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [usr, setUsr] = useState(null);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUsr(JSON.parse(userData));
    }
  }, []);

  useEffect(() => {
    if (usr && usr.type === 'MODERATOR') {
      const fetchUsers = async () => {
        try {
          const response = await fetch('http://localhost:8080/users/getAll');
          if (!response.ok) throw new Error('Failed to fetch');
          const data = await response.json();
          setUsers(data);
        } catch (error) {
          setError(error.message);
        } finally {
          setLoading(false);
        }
      };

      fetchUsers();
    } else {
      setLoading(false);
    }
  }, [usr]);

  const handleBanUser = async (userId) => {
    try {
      await fetch(`http://localhost:8080/users/ban?id=${userId}`, { method: 'PUT' });
      const updatedUsers = users.map((user) =>
        user.id === userId ? { ...user, banned: true } : user
      );
      setUsers(updatedUsers);
    } catch (error) {
      console.error('Error banning user:', error);
    }
  };

  const handleUnbanUser = async (userId) => {
    try {
      await fetch(`http://localhost:8080/users/unban?id=${userId}`, { method: 'PUT' });
      const updatedUsers = users.map((user) =>
        user.id === userId ? { ...user, banned: false } : user
      );
      setUsers(updatedUsers);
    } catch (error) {
      console.error('Error unbanning user:', error);
    }
  };

  if (loading) return <p>Loading users...</p>;
  if (error) return <p>Error loading users: {error}</p>;

  if (!usr || usr.type !== 'MODERATOR') {
    return <p>Access denied. You do not have permission to view this page.</p>;
  }

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
                    <Text size="sm" color="green">{user.type}</Text>
                  </div>
                </td>
                <td>
                  <Text size="sm">{user.creationDateTime}</Text>
                </td>
                <td>
                  <Button onClick={() => handleBanUser(user.id)} color="red" style={{ marginRight: '10px' }}>
                    Ban
                  </Button>
                  <Button onClick={() => handleUnbanUser(user.id)} color="green">
                    Unban
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </Container>
    </>
  );
}

export default Users;
