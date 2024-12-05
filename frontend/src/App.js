import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Transactions from './pages/Transactions';

const App = () => {
  return (
    <Router>
      <Routes>
        {/* Defina a rota e passe o componente correto usando a propriedade 'element' */}
        <Route path="/" element={<Home />} />
        <Route path="/transactions" element={<Transactions />} />
      </Routes>
    </Router>
  );
};

export default App;
