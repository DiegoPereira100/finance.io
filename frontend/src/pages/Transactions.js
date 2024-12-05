import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { RiMoneyDollarCircleLine } from "react-icons/ri";
import { PiUserCircleThin } from "react-icons/pi";
import { HiDotsVertical } from "react-icons/hi";
import axios from 'axios';
import './Style.css';

const Transactions = () => {
  const [transacoes, setTransacoes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [descricao, setDescricao] = useState('');
  const [valor, setValor] = useState('');
  const [tipo, setTipo] = useState('receita');
  const [contaSelecionada, setContaSelecionada] = useState('');
  const [contas, setContas] = useState([]);
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth());
  const [analise, setAnalise] = useState('');

  const formatDateForTable = (dateStr) => {
    const dateParts = dateStr.split('-');
    const date = new Date(dateParts[0], dateParts[1] - 1, dateParts[2]);
    return date.toLocaleDateString('pt-BR', { year: 'numeric', month: 'long', day: '2-digit' });
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

  const handleValorChange = (e) => {
    const newValue = parseFloat(e.target.value);
    setValor(isNaN(newValue) ? '' : newValue);
  };

  const buscarTransacoes = async () => {
    if (!contaSelecionada) {
      alert('Por favor, selecione uma conta.');
      return;
    }

    try {
      const url = `http://localhost:8080/conta/${contaSelecionada}`;
      const response = await axios.get(url);
      const { transacoes } = response.data;
      setTransacoes(transacoes || []);
    } catch (error) {
      console.error('Erro ao buscar transações:', error.response?.data || error.message);
      alert('Erro ao carregar transações. Tente novamente.');
    }
  };

  const resumoFi = {
    receitas: transacoes.filter(t => t.tipo === 'entrada').reduce((acc, t) => acc + t.valor, 0),
    transacoes: transacoes.reduce((acc, t) => acc + t.valor, 0),
    saldo: transacoes.filter(t => t.tipo === 'entrada').reduce((acc, t) => acc + t.valor, 0) - 
          transacoes.filter(t => t.tipo === 'saida').reduce((acc, t) => acc + t.valor, 0),
  };

  const currentDate = new Date();
  currentDate.setHours(0, 0, 0, 0);

  const convertToDate = (dateStr) => {
    const [day, month, year] = dateStr.split('/');
    return new Date(`${year}-${month}-${day}`);
  };

  const filteredThisMonth = transacoes.filter((transacao) => {
    const transacaoDate = convertToDate(transacao.data);
    return transacaoDate.getMonth() === selectedMonth && transacaoDate.getFullYear() === currentMonth.getFullYear();
  });

  const filteredToday = transacoes.filter((transacao) => {
    const transacaoDate = convertToDate(transacao.data);
    return transacaoDate.toLocaleDateString() === currentDate.toLocaleDateString();
  });

  useEffect(() => {
    axios.get('http://localhost:8080/transacao')
      .then(response => {
        setTransacoes(response.data);
      })
      .catch(error => {
        console.error('Erro ao carregar as transações:', error);
      })
      .finally(() => {
        setLoading(false);
      });

    axios.get('http://localhost:8080/conta')
      .then(response => {
        setContas(response.data);
      })
      .catch(error => {
        console.error('Erro ao carregar contas:', error);
      });
  }, []);

  const analisarConta = () => {
    setAnalise('');
    axios.post('http://localhost:8080/conta/analise-geral')
      .then((response) => {
        setAnalise(response.data);
      })
      .catch((error) => {
        console.error('Erro ao obter análise:', error);
        setAnalise('Erro ao obter análise.'); 
      });
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!contaSelecionada) {
      alert('Por favor, selecione uma conta.');
      return;
    }

    const novaTransacao = {
      descricao,
      valor: parseFloat(valor),
      tipo,
      contaId: contaSelecionada,
    };

    console.log('Dados da transação sendo enviados:', novaTransacao);

    try {
      const url = `http://localhost:8080/conta/${encodeURIComponent(contaSelecionada)}/transacao`;
      await axios.post(url, novaTransacao, {
        headers: { 'Content-Type': 'application/json' },
      });

      alert('Transação adicionada com sucesso!');
      setDescricao('');
      setValor('');
      setTipo('receita');
      setContaSelecionada('');
      buscarTransacoes();
    } catch (error) {
      console.error('Erro ao adicionar transação:', error.response?.data || error.message);
      alert(error.response?.data || 'Erro ao adicionar transação. Verifique os dados.');
    }
  };

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
              <h1 className='table-container-title'>Transações de Hoje</h1>
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
                  {filteredToday.map((transacao, index) => (
                    <tr key={index}>
                      <td className='tname'>{transacao.descricao}</td>
                      <td className='tname'>{transacao.tipo}</td>
                      <td className='tname'>{transacao.valor}</td>
                      <td className='tname'>{formatDateForTable(transacao.data)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="mt-3 table-container">
              <h1 className='table-container-title'>Transações de {currentMonthName}</h1>
              <table className="table table-dark table-hover">
                <thead>
                  <tr>
                    <th scope="col" className='table1-title-style'>Descrição</th>
                    <th scope="col" className='table1-title-style'>Tipo</th>
                    <th scope="col" className='table1-title-style'>Não Pago</th>
                    <th scope="col" className='table1-title-style'>Data</th>
                    <th scope="col" className='table1-title-style'></th>
                  </tr>
                </thead>
                <tbody>
                  {filteredThisMonth.map((transacao, index) => (
                    <tr key={index}>
                      <td className='tname'>{transacao.descricao}</td>
                      <td className='tname'>{transacao.tipo}</td>
                      <td className='tname'>{transacao.valor}</td>
                      <td className='tname'>{formatDateForTable(transacao.data)}</td>
                      <td className="tname table-actions-cell">
                        <button onClick={""} className="btn-warning btn-set">Editar</button>
                        <button onClick={""} className="btn-danger ml-2 btn-delete">Excluir</button>
                        <button onClick={() => analisarConta(transacao.idConta)} className="btn-info ml-2 btn-analise">Analisar</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <div className="mt-3 mb-5">
                {analise && (
                  <div className="card custom-card">
                    <div className="card-body">
                      <h5 className="card-title">Análise de Economia</h5>
                      <p className="card-text">{analise}</p>
                    </div>
                  </div>
                )}
              </div>

            </div>
          </div>
          <div className="col-4 transactions-resume">
            <div className="card custom-card">
              <div className="card-body text-center">
                <div className="card-text">
                  <p className="card-item">Total Transações</p>
                  <p className="card-value">{`R$ ${resumoFi.transacoes.toFixed(2)}`}</p>
                </div>
              </div>
            </div>
            <div className="card custom-card mt-3 mb-5">
              <div className="card-body">
                <h5 className="card-titleT">Adicionar Transação</h5>
                <form onSubmit={handleSubmit}>
                  <div className="form-group divT">
                    <label htmlFor="conta">Conta</label>
                    <select
                      id="conta"
                      className="form-control"
                      value={contaSelecionada}
                      onChange={(e) => setContaSelecionada(e.target.value)}
                    >
                      <option value="">Selecione uma conta</option>
                      {contas.map((conta) => (
                        <option key={conta.id} value={conta.id}>
                          {conta.id} - {conta.nome}
                        </option>
                      ))}
                    </select>

                  </div>
                  <div className="form-group divT">
                    <label htmlFor="tipo">Tipo de Transação</label>
                    <select
                      id="tipo"
                      className="form-control"
                      value={tipo}
                      onChange={(e) => setTipo(e.target.value)}
                    >
                      <option value="receita">Receita</option>
                      <option value="despesa">Despesa</option>
                    </select>
                  </div>
                  <div className="form-group divT">
                    <label htmlFor="descricao">Descrição</label>
                    <input
                      type="text"
                      id="descricao"
                      className="form-control"
                      value={descricao}
                      onChange={(e) => setDescricao(e.target.value)}
                      placeholder="Descrição da transação"
                    />
                  </div>
                  <div className="form-group divT">
                    <label htmlFor="valor">Valor</label>
                    <input
                      type="number"
                      id="valor"
                      className="form-control"
                      value={valor}
                      onChange={handleValorChange}
                      placeholder="Valor da transação"
                    />
                  </div>
                  <button type="submit" className="btn btn-primary mt-3 divT">Adicionar</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Transactions;
