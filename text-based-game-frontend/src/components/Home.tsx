import { useState } from 'react'
import Login from './Login'
import ThemeSelection from './ThemeSelection'
import Game from './Game'
import type { GameResponse } from '../types/GameResponse.tsx'

function Home() {
    const [loggedIn, setLoggedIn] = useState(false)
    const [gameResponse, setGameResponse] = useState<GameResponse | null>(null)

    return (
        <main>
            {!loggedIn ? (
                <>
                    <h1>Text Adventure Game</h1>

                    <h2>A Text-Based Adventure</h2>

                    <p>
                        Welcome to the text adventure! Log in or create an account to get started.
                    </p>

                    <Login onLogin={() => setLoggedIn(true)} />
                </>
            ) : gameResponse === null ? (
                <ThemeSelection
                    onGameStarted={(gameResponse) => setGameResponse(gameResponse)}
                />
            ) : (
                <Game gameResponse={gameResponse} />
            )}
        </main>
    )
}

export default Home