import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { RiMoneyDollarCircleLine } from "react-icons/ri";
import { PiUserCircleThin } from "react-icons/pi";
import { HiDotsVertical } from "react-icons/hi";
import './Style.css';

const contas = [
  { descricao: 'Banco do Brasil', receitas: 'R$ 2.500,00', transacoes: 'R$ 1.000,00', previsto: 'R$ 1.500,00', data: '2024-11-25' },
  { descricao: 'Caixa Econômica', receitas: 'R$ 1.000,00', transacoes: 'R$ 500,00', previsto: 'R$ 500,00', data: '2024-11-24' },
  { descricao: 'Itaú', receitas: 'R$ 3.200,00', transacoes: 'R$ 1.800,00', previsto: 'R$ 1.400,00', data: '2024-11-23' },
];

const saldos = [
  { conta: 'Poupança', valor: 1500.00 },
  { conta: 'Carteira', valor: 1200.50 },
];

const Home = () => {

  const total = saldos.reduce((acc, curr) => acc + curr.valor, 0);

  return (
    <div className='main-container'>
      <nav className="navbar d-block mb-5">
        <div className="nav-container">
          <div className='d-flex nav-logo-title'>
            <Link to="/" className="navbar-brand">
              <RiMoneyDollarCircleLine className='nav-logo' />
            </Link>
            <h1 className='nav-title-container'>Finance.io</h1>
          </div>
          <div className="nav-links-container">
            <Link to="/" className="nav-link">Visão Geral</Link>
            <Link to="/transactions" className="nav-link">Transações</Link>
          </div>
          <div className='nav-right-icons-container'>
            <HiDotsVertical className='nav-dots-icon' />
            <PiUserCircleThin className='nav-user-icon' />
          </div>
        </div>
      </nav>
      <div className="mt-5 table-container">
        <h1 className='table-container-title'>Contas</h1>
        <table className="table table-dark table-hover">
          <thead>
            <tr>
              <th scope="col" className='table1-title-style'>Descrição</th>
              <th scope="col" className='table1-title-style'>Receitas</th>
              <th scope="col" className='table1-title-style'>Transações</th>
              <th scope="col" className='table1-title-style'>Previsto</th>
              <th scope="col" className='table1-title-style'>Data</th>
            </tr>
          </thead>
          <tbody>
            {contas.map((conta, index) => (
              <tr key={index}>
                <td className='tname'>{conta.descricao}</td>
                <td className='tname'>{conta.receitas}</td>
                <td className='tname'>{conta.transacoes}</td>
                <td className='tname'>{conta.previsto}</td>
                <td className='tname'>{conta.data}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="mt-3 table-container">
        <h1 className='table-container-title'>Saldo em contas</h1>
        <table className="table table-dark table-hover">
          <tbody>
            <tr>
              <td className='table2-title-style'>Poupança</td>
              <td>R$ {saldos[0].valor.toFixed(2)}</td>
            </tr>
            <tr>
              <td className='table2-title-style'>Carteira</td>
              <td>R$ {saldos[1].valor.toFixed(2)}</td>
            </tr>
            <tr>
              <td><strong>Total</strong></td>
              <td><strong>R$ {total.toFixed(2)}</strong></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Home;
