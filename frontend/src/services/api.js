import axios from 'axios';

const api = axios.create({
  baseURL: 'https://luizaobackend-api.com',
});

export const getData = async () => {
  try {
    const response = await api.get('/data');
    return response.data;
  } catch (error) {
    console.error('Error when fetching data(que triste):', error);
    throw error;
  }
};
