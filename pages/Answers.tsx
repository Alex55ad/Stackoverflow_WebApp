import React, { useState, useEffect } from 'react';
import { Title, Text, Anchor, Container, Button, TextInput, Badge, Modal } from '@mantine/core';
import classes from './Test.module.css';
import { HeaderMegaMenu } from '../components/HeaderMegaMenu/HeaderMegaMenu';
import { ThumbsupIcon, ThumbsdownIcon } from '@primer/octicons-react'; // Import icons from Octicons
import { NavbarNested } from '../components/NavbarNested/NavbarNested';
import {ThumbsUpIcon} from "@storybook/icons";
import AnswerModal from '../components/NewAnswerModal';

export function Answers() {
  const [answers, setAnswers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [usr, setUser] = useState(null);
  const [modalOpened, setModalOpened] = useState(false);
  const [currentAnswer, setCurrentAnswer] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [editingAnswer, setEditingAnswer] = useState(null);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  useEffect(() => {
    const fetchAnswers = async () => {
      try {
        const response = await fetch('http://localhost:8080/answers/getAll');
        if (!response.ok) throw new Error('Failed to fetch answers');
        const data = await response.json();
        setAnswers(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    const fetchQuestions = async () => {
      try {
        const response = await fetch('http://localhost:8080/questions/getAll');
        if (!response.ok) throw new Error('Failed to fetch questions');
        const data = await response.json();
        setQuestions(data);
      } catch (error) {
        setError(error.message);
      }
    };

    fetchAnswers();
    fetchQuestions();
  }, []);

  
  const handleNewAnswerSubmit = async (answer) => {
    try {
      const response = await fetch('http://localhost:8080/answers/insert', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(answer),
      });
      if (!response.ok) throw new Error('Failed to create answer');
      const newAnswer = await response.json();
      setAnswers([newAnswer, ...answers]);
    } catch (error) {
      console.error('Error creating answer:', error);
    }finally {
      setEditingAnswer(null);
    }
    
  };

  const handleEditAnswerSubmit = async (answer) => {
    try {
      const response = await fetch(`http://localhost:8080/answers/update`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(answer),
      });
      if (!response.ok) throw new Error('Failed to update answer');
      const updatedAnswer = await response.json();
      setAnswers(answers.map(a => (a.id === updatedAnswer.id ? updatedAnswer : a)));
    } catch (error) {
      console.error('Error updating answer:', error);
    } finally {
      setEditingAnswer(null);
    }
  };
  
  const handleDeleteAnswer = async (id) => {
    try {
      await fetch(`http://localhost:8080/answers/deleteById?id=${id}`, {
        method: 'DELETE',
      });
      // Remove the deleted answer from the answers list
      setAnswers(answers.filter(answer => answer.id !== id));
    } catch (error) {
      console.error('Error deleting answer:', error);
    }
  };
  
  const handleEditClick = (answer) => {
    setEditingAnswer(answer);
    setModalOpened(true);
  };

  const handleUpvote = async (answerId) => {
    try {
      // Upvote the answer
      await fetch(`http://localhost:8080/answers/upvote/${answerId}?username=${usr.username}`, { method: 'POST' });
  
      // Fetch the updated answer data
      const response = await fetch(`http://localhost:8080/answers/getById/${answerId}`);
      if (!response.ok) throw new Error('Failed to fetch updated answer');
      const updatedAnswer = await response.json();
  
      // Update the answers list with the updated answer
      const updatedAnswers = answers.map((answer) =>
        answer.id === answerId ? updatedAnswer : answer
      );
      setAnswers(updatedAnswers);
    } catch (error) {
      console.error('Error upvoting answer:', error);
    }
  };
  
  const handleDownvote = async (answerId) => {
    try {
      // Downvote the answer
      await fetch(`http://localhost:8080/answers/downvote/${answerId}?username=${usr.username}`, { method: 'POST' });
  
      // Fetch the updated answer data
      const response = await fetch(`http://localhost:8080/answers/getById/${answerId}`);
      if (!response.ok) throw new Error('Failed to fetch updated answer');
      const updatedAnswer = await response.json();
  
      // Update the answers list with the updated answer
      const updatedAnswers = answers.map((answer) =>
        answer.id === answerId ? updatedAnswer : answer
      );
      setAnswers(updatedAnswers);
    } catch (error) {
      console.error('Error downvoting answer:', error);
    }
  };

  if (loading) return <p>Loading answers...</p>;
  if (error) return <p>Error loading answers: {error}</p>;

  return (
    <>
      <Title className={classes.title} ta="center" mt={100}>
        Stack
        <Text inherit variant="gradient" component="span" gradient={{ from: 'pink', to: 'yellow' }}>
          Underflow
        </Text>
      </Title>
      <HeaderMegaMenu />
      <Container size="md" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        {usr && (
          <Button onClick={() => setModalOpened(true)} style={{ marginBottom: '20px' }}>
            Post an Answer
          </Button>
        )}
        <AnswerModal
          opened={modalOpened}
          onClose={() => {
            setModalOpened(false);
            setCurrentAnswer(null);
          }}
          onSubmit={editingAnswer ? handleEditAnswerSubmit : handleNewAnswerSubmit}
          initialAnswer={editingAnswer}
          questions={questions}
          />
        <table style={{ width: '100%', textAlign: 'center' }}>
          <tbody>
            {answers.map((answer) => (
              <tr key={answer.id}>
                <td>
                  <img src={answer.pictureUrl} alt="Answer Image" style={{ width: '200px', height: '200px' }} />
                </td>
                <td style={{ textAlign: 'left', paddingBottom: '20px' }}>
                  <div>
                    <Text size="lg" style={{ fontSize: 20 }}>{answer.title}</Text>
                    <Text size="sm" color="purple">Re: {answer.question.title}</Text>
                    <Text size="sm" color="blue">{answer.author.username} {' (User Score:'} {answer.author.score}{')'}</Text>
                    <button onClick={() => handleUpvote(answer.id)} style={{ border: 'none', background: 'transparent' }}>
                      <ThumbsupIcon size={16} style={{ cursor: 'pointer', color: 'red' }} />
                    </button>
                    {answer.upvotes} {'   '}
                    <button onClick={() => handleDownvote(answer.id)} style={{ border: 'none', background: 'transparent' }}>
                      <ThumbsdownIcon size={16} style={{ cursor: 'pointer', color: 'blue' }} />
                    </button>
                    {answer.downvotes}
                    <Text size="sm">{answer.text}</Text>
                  </div>
                </td>
                <td>
                  <Text size="sm">{answer.formattedCreationDateTime}</Text>
                </td>
                <td>
                {usr && (usr.username === answer.author.username || usr.type === 'MODERATOR') && (
                  <td>
                    <Button onClick={() => handleEditClick(answer)} style={{ marginRight: '10px' }}>Edit</Button>
                    <Button onClick={() => handleDeleteAnswer(answer.id)} color="red">Delete</Button>
                  </td>
                )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </Container>
    </>
  );
}

export default Answers;
