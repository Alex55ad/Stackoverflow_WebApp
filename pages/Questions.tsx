import React, { useState, useEffect } from 'react';
import { Title, Text, Anchor, Container, Button, TextInput, Badge } from '@mantine/core';
import classes from './Test.module.css';
import { HeaderMegaMenu } from '../components/HeaderMegaMenu/HeaderMegaMenu';
import { ThumbsupIcon, ThumbsdownIcon } from '@primer/octicons-react'; // Import icons from Octicons
import { NavbarNested } from '../components/NavbarNested/NavbarNested';
import {ThumbsUpIcon} from "@storybook/icons";

export function Questions() {
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const response = await fetch('http://localhost:8080/questions/getAll');
        if (!response.ok) throw new Error('Failed to fetch');
        const data = await response.json();
        setQuestions(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchQuestions();
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
            {questions.map((question) => (
                <tr key={question.id}>
                  <td>
                    <img src={question.pictureUrl} alt="Question Image" style={{ width: '200px', height: '200px' }} />
                  </td>
                  <td style={{ textAlign: 'left', paddingBottom: '20px' }}>
                    <div>
                      <Text size="lg" style={{fontSize: 20}}>{question.title}</Text>
                      <Text size="sm" color="blue">{question.author.username} {' (User Score:'} {question.author.score}{')'}</Text>
                      <Text size="sm" color="green">{question.tags}</Text>
                      <ThumbsUpIcon size={16} /> {question.upvotes} {'   '}
                      <ThumbsdownIcon size={16} /> {question.downvotes}
                      <Text size="sm">{question.text}</Text>
                    </div>
                  </td>
                  <td>
                    <Text size="sm">{question.formattedCreationDateTime}</Text>
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
