import React, { useState, useEffect } from 'react';
import { Modal, TextInput, Textarea, Button, Select } from '@mantine/core';

function NewAnswerModal({ opened, onClose, onSubmit, initialAnswer, questions }) {
  const [title, setTitle] = useState('');
  const [text, setText] = useState('');
  const [pictureUrl, setPictureUrl] = useState('');
  const [question, setQuestion] = useState(null);
  const [answerId, setAnswerId] = useState(null);
  const [author, setAuthor] = useState('');

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      const user = JSON.parse(userData);
      if (user.type) {
        user.type = user.type.toUpperCase(); // Convert type to uppercase
      }
      setAuthor(user);
    }
  }, []);

  useEffect(() => {
    if (initialAnswer) {
      setTitle(initialAnswer.title);
      setText(initialAnswer.text);
      setPictureUrl(initialAnswer.pictureUrl);
      setQuestion(initialAnswer.question);
      setAnswerId(initialAnswer.id);
    } else {
      setTitle('');
      setText('');
      setPictureUrl('');
      setQuestion('');
      setAnswerId(null);
    }
  }, [initialAnswer]);

  const handleSubmit = () => {
    onSubmit({ id: answerId, question ,author, title, text, pictureUrl });
    onClose();
  };

  const handleQuestionChange = (questionId) => {
    const selectedQuestion = questions.find(q => q.id === parseInt(questionId, 10));
    setQuestion(selectedQuestion);
  };

  return (
    <Modal opened={opened} onClose={onClose} title="Answer">
      <Select
        data={questions.map(question => ({ value: question.id.toString(), label: question.title }))}
        value={question ? question.id.toString() : ''}
        onChange={handleQuestionChange}
        label="Select Question"
        placeholder="Select question to reply to"
        required
        fullWidth
        style={{ marginBottom: '10px' }}
      />
        <TextInput
        label="Username"
        placeholder="Your username"
        value={author.username}
        onChange={(event) => setAuthor(event.currentTarget.value)}
        disabled
      />
      <TextInput
        label="Title"
        placeholder="Enter answer title"
        value={title}
        onChange={(event) => setTitle(event.currentTarget.value)}
      />
      <Textarea
        label="Text"
        placeholder="Enter answer details"
        value={text}
        onChange={(event) => setText(event.currentTarget.value)}
      />
      <TextInput
        label="Picture URL"
        placeholder="Enter picture URL"
        value={pictureUrl}
        onChange={(event) => setPictureUrl(event.currentTarget.value)}
      />
      <Button onClick={handleSubmit} style={{ marginTop: '10px' }}>Submit</Button>
    </Modal>
  );
}

export default NewAnswerModal;
