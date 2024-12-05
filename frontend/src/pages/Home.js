import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { RiMoneyDollarCircleLine } from "react-icons/ri";
import { PiUserCircleThin } from "react-icons/pi";
import { HiDotsVertical } from "react-icons/hi";
import axios from 'axios';
import './Style.css';

const Home = () => {
  const [contas, setContas] = useState([]);
  const [transacoes, setTransacoes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState({ nome: '', saldo: 0 });
  const [showOffcanvas, setShowOffcanvas] = useState(false);

  useEffect(() => {
    axios.get('http://localhost:8080/conta')
      .then(response => {
        setContas(response.data);
      })
      .catch(error => {
        console.error('Erro ao carregar as contas:', error);
      });

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
  }, []);

  if (loading) {
    return <div>Carregando...</div>;
  }

  const totalSaldo = contas.reduce((acc, conta) => acc + conta.saldo, 0);

  const getReceitasTransacoes = (contaId) => {
    const transacoesConta = transacoes.filter(transacao => transacao.conta?.idConta === contaId);
    const receitas = transacoesConta.filter(t => t.tipo === 'entrada').reduce((acc, t) => acc + t.valor, 0);
    const transacoesTotal = transacoesConta.reduce((acc, t) => acc + t.valor, 0);
    const previsto = receitas - transacoesTotal;

    const datas = transacoesConta.map(transacao => {
      const data = new Date(transacao.data);
      return data.toLocaleDateString();
    });

    return { receitas, transacoes: transacoesTotal, previsto, datas };
  };

  const deleteConta = (id) => {
    axios.delete(`http://localhost:8080/conta/${id}`)
      .then(() => {
        setContas(contas.filter(conta => conta.idConta !== id));
      })
      .catch(error => {
        console.error("Erro ao excluir conta:", error);
      });
  };

  const editConta = (conta) => {
    setEditing(conta.idConta);
    setFormData({ nome: conta.nome, saldo: conta.saldo });
  };

  const saveConta = (id) => {
    axios.put(`http://localhost:8080/conta/${id}`, formData)
      .then(response => {
        setContas(contas.map(conta => conta.idConta === id ? { ...conta, ...formData } : conta));
        setEditing(null);
      })
      .catch(error => {
        console.error("Erro ao atualizar conta:", error);
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'saldo' ? parseFloat(value) : value,
    }));
  };

  const handleAddConta = (e) => {
    e.preventDefault();
    axios.post('http://localhost:8080/conta', formData)
      .then(response => {
        setContas([...contas, response.data]);
        setShowOffcanvas(false);
        setFormData({ nome: '', saldo: 0 });
      })
      .catch(error => {
        console.error("Erro ao adicionar conta:", error);
      });
  };

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
        <h1 className="table-container-title">
          Contas
          <button onClick={() => setShowOffcanvas(true)} className="btn-add-account ml-3">
            +
          </button>
        </h1>
        <table className="table table-dark table-hover">
          <thead>
            <tr>
              <th scope="col" className="table1-title-style">Nome do Banco</th>
              <th scope="col" className="table1-title-style">Saldo</th>
              <th scope="col" className="table1-title-style"></th>
            </tr>
          </thead>
          <tbody>
            {contas.map((conta, index) => {
              const { receitas, transacoes, previsto, datas } = getReceitasTransacoes(conta.idConta);
              const saldoFormatted = parseFloat(conta.saldo).toFixed(2);
              return (
                <tr key={index}>
                  <td className="tname">{conta.nome || "Sem nome de banco"}</td>
                  <td className="tname">{saldoFormatted}</td>
                  <td className="tname table-actions-cell">
                    <button onClick={() => editConta(conta)} className="btn-warning btn-set">Editar</button>
                    <button onClick={() => deleteConta(conta.idConta)} className="btn-danger ml-2 btn-delete">Excluir</button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
      <div className="mt-3 table-container">
        <h1 className='table-container-title'>Saldo em contas</h1>
        <table className="table table-dark table-hover">
          <tbody>
            {contas.map((conta, index) => (
              <tr key={index}>
                <td className='table2-title-style'>{conta.nome || "Sem nome de banco"}</td>
                <td>R$ {conta.saldo.toFixed(2)}</td>
              </tr>
            ))}
            <tr>
              <td><strong>Total</strong></td>
              <td><strong>R$ {totalSaldo.toFixed(2)}</strong></td>
            </tr>
          </tbody>
        </table>
      </div>
      {editing && (
        <div className="modal" tabIndex="-1" style={{ display: 'block' }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Editar Conta</h5>
                <button type="button" className="btn-close" onClick={() => setEditing(null)}></button>
              </div>
              <div className="modal-body">
                <form onSubmit={(e) => { e.preventDefault(); saveConta(editing); }}>
                  <div className="mb-3">
                    <label htmlFor="nome" className="form-label">Nome do Banco</label>
                    <input
                      type="text"
                      className="form-control"
                      id="nome"
                      name="nome"
                      value={formData.nome}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="saldo" className="form-label">Saldo</label>
                    <input
                      type="number"
                      className="form-control"
                      id="saldo"
                      name="saldo"
                      value={formData.saldo}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                  <button type="submit" className="btn btn-primary">Salvar</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      )}
      <div className={`offcanvas offcanvas-end ${showOffcanvas ? 'show' : ''}`} tabIndex="-1" id="offcanvasAddConta" aria-labelledby="offcanvasAddContaLabel">
        <div className="offcanvas-header">
          <h5 id="offcanvasAddContaLabel">Adicionar Conta</h5>
          <button type="button" className="btn-close" onClick={() => setShowOffcanvas(false)}></button>
        </div>
        <div className="offcanvas-body">
          <form onSubmit={handleAddConta}>
            <div className="mb-3">
              <label htmlFor="nome" className="form-label">Nome do Banco</label>
              <input
                type="text"
                className="form-control"
                id="nome"
                name="nome"
                value={formData.nome}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="saldo" className="form-label">Saldo</label>
              <input
                type="number"
                className="form-control"
                id="saldo"
                name="saldo"
                value={formData.saldo}
                onChange={handleInputChange}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary">Salvar</button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Home;
