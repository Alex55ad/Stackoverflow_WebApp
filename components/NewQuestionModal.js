import React, { useState, useEffect } from 'react';
import { Modal, TextInput, Textarea, Button } from '@mantine/core';

function NewQuestionModal({ opened, onClose, onSubmit, initialQuestion}) {
  const [title, setTitle] = useState('');
  const [text, setText] = useState('');
  const [pictureUrl, setPictureUrl] = useState('');
  const [tags, setTags] = useState('');
  const [author, setAuthor] = useState('');
  const [questionId, setQuestionId] = useState(null);
  
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
    if (initialQuestion) {
      setTitle(initialQuestion.title);
      setText(initialQuestion.text);
      setPictureUrl(initialQuestion.pictureUrl);
      setTags(initialQuestion.tags);
      setQuestionId(initialQuestion.id);
    } else {
      setTitle('');
      setText('');
      setPictureUrl('');
      setTags('');
      setQuestionId(null);
    }
  }, [initialQuestion]);


  const handleSubmit = () => {
    onSubmit({ id: questionId, author, title, text, pictureUrl, tags});
    onClose();
  };


  return (
    <Modal opened={opened} onClose={onClose} title="Post a new question">
        <TextInput
        label="Username"
        placeholder="Your username"
        value={author.username}
        onChange={(event) => setAuthor(event.currentTarget.value)}
        disabled
      />
      <TextInput
        label="Title"
        placeholder="Enter question title"
        value={title}
        onChange={(event) => setTitle(event.currentTarget.value)}
      />
      <Textarea
        label="Text"
        placeholder="Enter question details"
        value={text}
        onChange={(event) => setText(event.currentTarget.value)}
      />
      <TextInput
        label="Picture URL"
        placeholder="Enter picture URL"
        value={pictureUrl}
        onChange={(event) => setPictureUrl(event.currentTarget.value)}
      />
      <TextInput
        label="Tags"
        placeholder="Enter tags"
        value={tags}
        onChange={(event) => setTags(event.currentTarget.value)}
      />
      <Button onClick={handleSubmit} style={{ marginTop: '10px' }}>Submit</Button>
    </Modal>
  );
}

export default NewQuestionModal;