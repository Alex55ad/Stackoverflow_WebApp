import React, { useState, useEffect } from 'react';
import { Title, Text, Anchor, Container, Button, TextInput, Badge } from '@mantine/core';
import classes from './Test.module.css';
import { HeaderMegaMenu } from '../components/HeaderMegaMenu/HeaderMegaMenu';
import { ThumbsupIcon, ThumbsdownIcon } from '@primer/octicons-react'; // Import icons from Octicons
import { NavbarNested } from '../components/NavbarNested/NavbarNested';
import {ThumbsUpIcon} from "@storybook/icons";

export function Questions() {
  const [answers, setAnswers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAnswers = async () => {
      try {
        const response = await fetch('http://localhost:8080/answers/getAllAnswers');
        if (!response.ok) throw new Error('Failed to fetch');
        const data = await response.json();
        setAnswers(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchAnswers();
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
            {answers.map((answer) => (
                <tr key={answer.id}>
                  <td style={{ textAlign: 'left', paddingBottom: '20px' }}>
                    <div>
                      <Text size="lg" weight="bold">{answer.title}</Text>
                      <img src={answer.imageUrl} alt="Answer Image" />
                      <Text size="sm" color="purple">Re: {answer.question.title}</Text>
                      <Text size="sm" color="blue">{answer.author.username} {' ('} {answer.author.score} {')'}</Text>
                      <ThumbsUpIcon size={16} /> {answer.upvotes} {'   '}
                      <ThumbsdownIcon size={16} /> {answer.downvotes}
                      <Text size="sm">{answer.text}</Text>
                    </div>
                  </td>
                  <td>
                    <Text size="sm">{answer.creationDateTime}</Text>
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
