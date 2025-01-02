import React, { useState } from "react";

function App() {
  // Стан для зберігання введених даних
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
    name: "",
    surname: "",
    phoneNumber: "",
    balance: "",
  });

  const [errorMessage, setErrorMessage] = useState("");

  // Функція для обробки змін в полях форми
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // Функція для відправки форми
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Надсилаємо запит на сервер
    try {
      const response = await fetch("http://localhost:8085/createUser", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error("Error: " + (await response.text()));
      }

      alert("User created successfully!");
    } catch (error) {
      setErrorMessage(error.message);
    }
  };

  return (
    <div className="App">
      <h1>User Registration</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Username:
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
          />
        </label>
        <br />
        <label>
          Password:
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
          />
        </label>
        <br />
        <label>
          Email:
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
          />
        </label>
        <br />
        <label>
          Name:
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
          />
        </label>
        <br />
        <label>
          Surname:
          <input
            type="text"
            name="surname"
            value={formData.surname}
            onChange={handleChange}
          />
        </label>
        <br />
        <label>
          Phone Number:
          <input
            type="text"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={handleChange}
          />
        </label>
        <br />
        <label>
          Balance:
          <input
            type="text"
            name="balance"
            value={formData.balance}
            onChange={handleChange}
          />
        </label>
        <br />
        <button type="submit">Create User</button>
      </form>

      {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
    </div>
  );
}

export default App;
