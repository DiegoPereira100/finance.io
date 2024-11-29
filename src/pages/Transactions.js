import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { RiMoneyDollarCircleLine } from "react-icons/ri";
import { PiUserCircleThin } from "react-icons/pi";
import { HiDotsVertical } from "react-icons/hi";
import './Style.css';

const despesas = [
  { descricao: 'Gasolina', tipo: 'Transporte', naoPago: '-500,00', data: '2024-10-28T12:00:00' },
  { descricao: 'Compras semanais', tipo: 'Mercado', naoPago: '-754,25', data: '2024-10-28T14:00:00' },
  { descricao: 'Almoço de trabalho', tipo: 'Alimentação', naoPago: '-120,00', data: '2024-12-28T12:00:00' },
  { descricao: 'Café da manhã', tipo: 'Alimentação', naoPago: '-25,00', data: '2024-11-18T08:00:00' },
  { descricao: 'Cantina da escola do filhinho', tipo: 'Alimentação', naoPago: '-50,00', data: '2024-07-05T12:00:00' },
];

const assinaturas = [
  { descricao: 'Assinatura Amazon Prime', tipo: 'Assinaturas e serviços', naoPago: '-19,90', data: '2024-11-25T09:00:00' },
  { descricao: 'Assinatura Netflix', tipo: 'Assinaturas e serviços', naoPago: '-45,00', data: '2024-11-29T11:30:00' },
  { descricao: 'Spotify', tipo: 'Assinaturas e serviços', naoPago: '-29,90', data: '2024-11-28T12:00:00' },
  { descricao: 'Assinatura YouTube Premium', tipo: 'Assinaturas e serviços', naoPago: '-34,90', data: '2024-11-29T10:00:00' },
  { descricao: 'Assinatura Disney+', tipo: 'Assinaturas e serviços', naoPago: '-49,90', data: '2024-11-27T15:00:00' },
];

const resumoFi = {
  receitas: 'R$ 2.500,00',
  transacoes: 'R$ -1.324,15',
  saldo: 'R$ 1.175,85',
};

const Transactions = () => {
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth());

  const formatDate = (date) => {
    const options = { day: '2-digit', month: 'long' };
    return new Date(date).toLocaleDateString('pt-BR', options);
  };

  const formatDateForTable = (date) => {
    const options = { year: 'numeric', month: 'long', day: '2-digit' };
    return new Date(date).toLocaleDateString('pt-BR', options);
  };

  const handleNextMonth = () => {
    const newMonth = new Date(currentMonth);
    newMonth.setMonth(currentMonth.getMonth() + 1);
    setCurrentMonth(newMonth);
    setSelectedMonth(newMonth.getMonth());
  };

  const handlePrevMonth = () => {
    const newMonth = new Date(currentMonth);
    newMonth.setMonth(currentMonth.getMonth() - 1);
    setCurrentMonth(newMonth);
    setSelectedMonth(newMonth.getMonth());
  };

  const currentDate = new Date();
  currentDate.setHours(0, 0, 0, 0);

  const filteredDespesasToday = despesas.filter((despesa) => {
    const despesaDate = new Date(despesa.data);
    return despesaDate.getDate() === currentDate.getDate() &&
      despesaDate.getMonth() === currentDate.getMonth() &&
      despesaDate.getFullYear() === currentDate.getFullYear();
  });

  const filteredAssinaturasToday = assinaturas.filter((assinatura) => {
    const assinaturaDate = new Date(assinatura.data);
    return assinaturaDate.getDate() === currentDate.getDate() &&
      assinaturaDate.getMonth() === currentDate.getMonth() &&
      assinaturaDate.getFullYear() === currentDate.getFullYear();
  });

  const filteredDespesasMonth = despesas.filter((despesa) => {
    const despesaDate = new Date(despesa.data);
    return despesaDate.getMonth() === selectedMonth;
  });

  const filteredAssinaturasMonth = assinaturas.filter((assinatura) => {
    const assinaturaDate = new Date(assinatura.data);
    return assinaturaDate.getMonth() === selectedMonth;
  });

  const currentMonthName = currentMonth.toLocaleString('pt-BR', { month: 'long' }).charAt(0).toUpperCase() + currentMonth.toLocaleString('pt-BR', { month: 'long' }).slice(1);

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
      <div className="main-content">
        <div className="row">
          <div className="col-8 tablesT-container">
            <div className="mt-3 month-day-panel">
              <button className="month-btn" onClick={handlePrevMonth}>←</button>
              <h1 className='month-title'>{currentMonthName}</h1>
              <button className="month-btn" onClick={handleNextMonth}>→</button>
            </div>
            <div className="mt-3 table-container">
              <h1 className='table-container-title'>
                Transações de Hoje
              </h1>
              <table className="table table-dark table-hover">
                <thead>
                  <tr>
                    <th scope="col" className='table1-title-style'>Descrição</th>
                    <th scope="col" className='table1-title-style'>Tipo</th>
                    <th scope="col" className='table1-title-style'>Não Pago</th>
                    <th scope="col" className='table1-title-style'>Data</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredDespesasToday.map((despesa, index) => (
                    <tr key={index}>
                      <td className='tname'>{despesa.descricao}</td>
                      <td className='tname'>{despesa.tipo}</td>
                      <td className='tname'>{despesa.naoPago}</td>
                      <td className='tname'>{formatDateForTable(despesa.data)}</td>
                    </tr>
                  ))}
                  {filteredAssinaturasToday.map((assinatura, index) => (
                    <tr key={index}>
                      <td className='tname'>{assinatura.descricao}</td>
                      <td className='tname'>{assinatura.tipo}</td>
                      <td className='tname'>{assinatura.naoPago}</td>
                      <td className='tname'>{formatDateForTable(assinatura.data)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="mt-3 table-container">
              <h1 className='table-container-title'>
                Transações de {currentMonthName}
              </h1>
              <table className="table table-dark table-hover">
                <thead>
                  <tr>
                    <th scope="col" className='table1-title-style'>Descrição</th>
                    <th scope="col" className='table1-title-style'>Tipo</th>
                    <th scope="col" className='table1-title-style'>Não Pago</th>
                    <th scope="col" className='table1-title-style'>Data</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredDespesasMonth.map((despesa, index) => (
                    <tr key={index}>
                      <td className='tname'>{despesa.descricao}</td>
                      <td className='tname'>{despesa.tipo}</td>
                      <td className='tname'>{despesa.naoPago}</td>
                      <td className='tname'>{formatDateForTable(despesa.data)}</td>
                    </tr>
                  ))}
                  {filteredAssinaturasMonth.map((assinatura, index) => (
                    <tr key={index}>
                      <td className='tname'>{assinatura.descricao}</td>
                      <td className='tname'>{assinatura.tipo}</td>
                      <td className='tname'>{assinatura.naoPago}</td>
                      <td className='tname'>{formatDateForTable(assinatura.data)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
          <div className="col-4 transactions-resume">
            <div className="card custom-card">
              <div className="card-body text-center">
                <div className="card-text">
                  <p className="card-item">Receitas</p>
                  <p className="card-value mb-5 pb-5">{resumoFi.receitas}</p>
                  <p className="card-item">Transações</p>
                  <p className="card-value mb-5 pb-5">{resumoFi.transacoes}</p>
                  <p className="card-item">Saldo</p>
                  <p className="card-value">{resumoFi.saldo}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Transactions;
