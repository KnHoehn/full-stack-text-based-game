import { useState } from 'react'
import Login from './Login'
import ThemeSelection from './ThemeSelection'

function Home() {

    const [loggedIn, setLoggedIn] = useState(false)

    return (
        <main>
            <h1>Text Adventure Game</h1>

            <h2>A Text-Based Adventure</h2>

            <p>
                Welcome to the text adventure! Log in or create an account to get started.
            </p>

            {loggedIn ? (
                <ThemeSelection
                    onGameStarted={(gameState) => console.log(gameState)}
                />
            ) : (
                <Login onLogin={() => setLoggedIn(true)} />
            )}
        </main>
    )
}

export default Home