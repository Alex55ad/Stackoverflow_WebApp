import React, { useState, useEffect } from 'react';
import { Title, Text, Anchor, Container, Button, TextInput, Badge, Modal } from '@mantine/core';
import classes from './Test.module.css';
import { HeaderMegaMenu } from '../components/HeaderMegaMenu/HeaderMegaMenu';
import { ThumbsupIcon, ThumbsdownIcon } from '@primer/octicons-react'; // Import icons from Octicons
import { NavbarNested } from '../components/NavbarNested/NavbarNested';
import {ThumbsUpIcon} from "@storybook/icons";
import NewQuestionModal from '../components/NewQuestionModal';

export function Questions() {
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [usr, setUser] = useState(null);
  const [modalOpened, setModalOpened] = useState(false);
  const [editingQuestion, setEditingQuestion] = useState(null);
  const [search, setSearch] = useState('');
  const [filterType, setFilterType] = useState('');

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  useEffect(() => {
    fetchAllQuestions();
  }, []);

  const fetchAllQuestions = async () => {
    setLoading(true);
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

  const handleNewQuestionSubmit = async (question) => {
    try {

      const response = await fetch('http://localhost:8080/questions/insert', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(question),
      });
      if (!response.ok) throw new Error('Failed to create question');
      const newQuestion = await response.json();
      setQuestions([newQuestion, ...questions]);
    } catch (error) {
      console.error('Error creating question:', error);
    }finally {
      setEditingQuestion(null);
    }
  };

  const handleEditQuestionSubmit = async (question) => {
    try {
      const response = await fetch(`http://localhost:8080/questions/update`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(question),
      });
      if (!response.ok) throw new Error('Failed to update question');
      const updatedQuestion = await response.json();
      setQuestions(questions.map(q => (q.id === updatedQuestion.id ? updatedQuestion : q)));
    } catch (error) {
      console.error('Error updating question:', error);
    } finally {
      setEditingQuestion(null);
    }
  };

  const handleDeleteQuestion = async (id) => {
    try {
      await fetch(`http://localhost:8080/questions/deleteById?id=${id}`, {
        method: 'DELETE',
      });
      setQuestions(questions.filter(question => question.id !== id));
    } catch (error) {
      console.error('Error deleting question:', error);
    }
  };

  const handleEditClick = (question) => {
    setEditingQuestion(question);
    setModalOpened(true);
  };


  const handleUpvote = async (questionId) => {
    try {
      // Upvote the question
      await fetch(`http://localhost:8080/questions/upvote/${questionId}?username=${usr.username}`, { method: 'POST' });
  
      // Fetch the updated question data
      const response = await fetch(`http://localhost:8080/questions/getById/${questionId}`);
      if (!response.ok) throw new Error('Failed to fetch updated question');
      const updatedQuestion = await response.json();
  
      // Update the questions list with the updated question
      const updatedQuestions = questions.map((question) =>
        question.id === questionId ? updatedQuestion : question
      );
      setQuestions(updatedQuestions);
    } catch (error) {
      console.error('Error upvoting question:', error);
    }
  };
  
  const handleDownvote = async (questionId) => {
    try {
      // Downvote the question
      await fetch(`http://localhost:8080/questions/downvote/${questionId}?username=${usr.username}`, { method: 'POST' });
  
      // Fetch the updated question data
      const response = await fetch(`http://localhost:8080/questions/getById/${questionId}`);
      if (!response.ok) throw new Error('Failed to fetch updated question');
      const updatedQuestion = await response.json();
  
      // Update the questions list with the updated question
      const updatedQuestions = questions.map((question) =>
        question.id === questionId ? updatedQuestion : question
      );
      setQuestions(updatedQuestions);
    } catch (error) {
      console.error('Error downvoting question:', error);
    }
  };

  useEffect(() => {
    const updateUserScores = async () => {
      try {
        // Loop through each question and extract the username of the author
        const usernames = questions.map(question => question.author.username);
        
        for (const username of usernames) {
          await fetch(`http://localhost:8080/users/calculateScore?username=${username}`,{method: 'POST',});
        }
      } catch (error) {
        console.error('Error updating user scores:', error);
      }
    };

    // Call the function to update user scores when questions change
    if (questions.length > 0) {
      updateUserScores();
    }
  }, [questions]);

  const handleSearch = async () => {
    setLoading(true);
    try {
      let response;
      if (filterType === 'tag') {
        response = await fetch(`http://localhost:8080/questions/byTag/${search}`);
      } else if (filterType === 'username') {
        response = await fetch(`http://localhost:8080/questions/byUser/${search}`);
      } else {
        response = await fetch(`http://localhost:8080/questions/getAll`);
      }
      if (!response.ok) throw new Error('Failed to fetch questions');
      const data = await response.json();
      setQuestions(data);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleMyQuestions = async () => {
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/questions/byUser/${usr.username}`);
      if (!response.ok) throw new Error('Failed to fetch questions');
      const data = await response.json();
      setQuestions(data);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };


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
      <Container size="md" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      {usr && (
        <Button onClick={() => setModalOpened(true)} style={{ marginBottom: '20px' }}>Post a Question</Button>
      )}
        <NewQuestionModal
          opened={modalOpened}
          onClose={() => setModalOpened(false)}
          onSubmit={editingQuestion ? handleEditQuestionSubmit : handleNewQuestionSubmit}
          author={usr?.username}
          initialQuestion={editingQuestion}
        />
                <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', marginBottom: '20px' }}>
          <TextInput
            placeholder="Search..."
            value={search}
            onChange={(e) => setSearch(e.currentTarget.value)}
            style={{ marginRight: '10px' }}
          />
          <Button onClick={() => { setFilterType('tag'); handleSearch(); }} style={{ marginRight: '10px' }}>Search by Tag</Button>
          <Button onClick={() => { setFilterType('username'); handleSearch(); }} style={{ marginRight: '10px' }}>Search by Username</Button>
          <Button onClick={() => { fetchAllQuestions();}} style={{ marginRight: '10px' }}>All questions</Button>
          {usr && (
            <Button onClick={handleMyQuestions}>My Questions</Button>
          )}
        </div>
        <table style={{ width: '100%', textAlign: 'center' }}>
          <tbody>
            {questions.map((question) => (
              <tr key={question.id}>
                <td>
                  <img src={question.pictureUrl} alt="Question Image" style={{ width: '200px', height: '200px' }} />
                </td>
                <td style={{ textAlign: 'left', paddingBottom: '20px' }}>
                  <div>
                    <Text size="lg" style={{ fontSize: 20 }}>{question.title}</Text>
                    <Text size="sm" color="blue">{question.author.username} {' (User Score:'} {question.author.score}{')'}</Text>
                    <Text size="sm" color="green">{question.tags}</Text>
                    <button onClick={() => handleUpvote(question.id)} style={{ border: 'none', background: 'transparent' }}>
                      <ThumbsupIcon size={16} style={{ cursor: 'pointer', color: 'red' }} />
                    </button>
                    {question.upvotes} {'   '}
                    <button onClick={() => handleDownvote(question.id)} style={{ border: 'none', background: 'transparent' }}>
                      <ThumbsdownIcon size={16} style={{ cursor: 'pointer', color: 'blue' }} />
                    </button>
                    {question.downvotes}
                    <Text size="sm">{question.text}</Text>
                  </div>
                </td>
                <td>
                  <Text size="sm">{question.formattedCreationDateTime}</Text>
                </td>
                {usr && (usr.username === question.author.username || usr.type === 'MODERATOR') && (
                  <td>
                    <Button onClick={() => handleEditClick(question)} style={{ marginRight: '10px' }}>Edit</Button>
                    <Button onClick={() => handleDeleteQuestion(question.id)} color="red">Delete</Button>
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </Container>
    </>
  );
}
export default Questions;
