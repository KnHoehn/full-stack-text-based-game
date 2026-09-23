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

            <label htmlFor="username">Username</label>
            <input
                id="username"
                type="text"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
            />

            <label htmlFor="password">Password</label>
            <input
                id="password"
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
            />

            <button onClick={handleLogin}>Log In</button>

            <button onClick={handleCreateAccount}>Create Account</button>

            <p>{message}</p>
        </div>
    )
}

export default Login
