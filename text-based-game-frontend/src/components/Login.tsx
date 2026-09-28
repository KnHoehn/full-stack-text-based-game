import { useState } from 'react'

type LoginProps = {
    onLogin: () => void
}

function Login({ onLogin }: LoginProps) {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [message, setMessage] = useState('')

    async function getCurrentUser() {
        try {
            const token = localStorage.getItem('token')

            const response = await fetch('/api/user/me', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })

            if (!response.ok) {
                setMessage('Unable to retrieve user information.')
                return
            }

            const result = await response.json()

            setMessage(`Logged in as ${result.userName}`)

        } catch (error) {
            console.error(error)
            setMessage('Unable to connect to the server.')
        }
    }

    async function handleLogin() {

        if (username.trim() === '' || password.trim() === '') {
            setMessage('Username and password are required.')
            return
        }

        try {
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userName: username,
                    password: password
                })
            })

            if (!response.ok) {
                setMessage('Invalid username or password.')
                return
            }

            const result = await response.json()

            localStorage.setItem('token', result.token)

            await getCurrentUser()

            onLogin()

        } catch (error) {
            console.error(error)
            setMessage('Unable to connect to the server.')
        }
    }

    async function handleCreateAccount() {

        if (username.trim() === '' || password.trim() === '') {
            setMessage('Username and password are required.')
            return
        }

        try {
            const response = await fetch('/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userName: username,
                    password: password
                })
            })

            const result = await response.json()

            if (!response.ok) {
                setMessage(result.message)
                return
            }

            setMessage(result.message)

        } catch (error) {
            console.error(error)
            setMessage('Unable to connect to the server.')
        }
    }

    return (
        <div>
            <h2>Log In</h2>

            <div className="login-line">
                <label htmlFor="username">Username:</label>

                <span>&gt;</span>

                <input
                    id="username"
                    type="text"
                    value={username}
                    onChange={(event) => setUsername(event.target.value)}
                    autoFocus
                />
            </div>

            <div className="login-line">
                <label htmlFor="password">Password:</label>

                <span>&gt;</span>

                <input
                    id="password"
                    type="password"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                />
            </div>

            <div className="login-actions">
                <button onClick={handleLogin}>
                    &gt; Log In
                </button>

                <button onClick={handleCreateAccount}>
                    &gt; Create Account
                </button>
            </div>

            <p>{message}</p>
        </div>
    )

}

export default Login
