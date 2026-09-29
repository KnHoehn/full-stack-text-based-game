import { useState } from 'react'
import Login from './Login'
import ThemeSelection from './ThemeSelection'
import Game from './Game'
import type { GameResponse } from '../types/GameResponse.tsx'

function Home() {
    const [loggedIn, setLoggedIn] = useState(false)
    const [gameResponse, setGameResponse] = useState<GameResponse | null>(null)

    function handleLogout() {
        localStorage.removeItem('token')
        setLoggedIn(false)
    }

    let content

    if (loggedIn) {
        if (gameResponse === null) {
            content = (
                <ThemeSelection
                    onGameStarted={(response) => setGameResponse(response)}
                    onLogout={handleLogout}
                />
            )
        } else {
            content = (
                <Game
                    gameResponse={gameResponse}
                    onGameExit={() => setGameResponse(null)}
                />
            )
        }
    } else {
        content = (
            <>
                <h1>Text Adventure Game</h1>

                <p>
                    Welcome to Text Adventure, the text-based adventure game! Log in or create an account to get
                    started.
                </p>

                <Login onLogin={() => setLoggedIn(true)} />
            </>
        )
    }

    return (
        <main>
            {content}
        </main>
    )
}

export default Home