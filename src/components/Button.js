import React from "react";
import './Button.css';

const Button = ({ text, onclick }) => {
  return (
    <button onClick={onclick} className="btn">
      {text}
    </button>
  );
};

export default Button;