import Game from './Game'
import { useState } from 'react'
import Login from './Login'
import ThemeSelection from './ThemeSelection'
import type { GameState } from './types/GameState'

function Home() {

    const [loggedIn, setLoggedIn] = useState(false)

    const [gameState, setGameState] = useState<GameState | null>(null)

    return (
        <main>
            <h1>Text Adventure Game</h1>

            <h2>A Text-Based Adventure</h2>

            <p>
                Welcome to the text adventure! Log in or create an account to get started.
            </p>

            {!loggedIn ? (
                <Login onLogin={() => setLoggedIn(true)} />
            ) : gameState === null ? (
                <ThemeSelection
                    onGameStarted={(gameState) => setGameState(gameState)}
                />
            ) : (
                <Game gameState={gameState} />
            )}
        </main>
    )
}

export default Home