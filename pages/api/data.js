// pages/api/data.js

import fetch from 'isomorphic-unfetch';

export default async function handler(req, res) {
  try {
    const response = await fetch('http://localhost:8080/delivery/getAll');
    const data = await response.json();
    res.status(200).json(data);
  } catch (error) {
    console.error('Error fetching data:', error);
    res.status(500).json({ error: 'Error fetching data' });
  }
}
