import type { GameResponse } from '../types/GameResponse.tsx'
import Scoreboard from './Scoreboard'

type ThemeSelectionProps = {
    onGameStarted: (gameResponse: GameResponse) => void
    onLogout: () => void
}

function ThemeSelection({ onGameStarted, onLogout }: ThemeSelectionProps) {

    async function startGame(theme: string) {

        try {
            const token = localStorage.getItem('token')

            const response = await fetch('/api/games', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    theme: theme
                })
            })

            if (!response.ok) {
                console.error('Unable to start game')
                return
            }

            const gameResponse: GameResponse = await response.json()

            onGameStarted(gameResponse)

        } catch (error) {
            console.error(error)
        }
    }


    return (
        <div>
            <h2>Choose Your Game Theme</h2>

            <div className="theme-actions">
                <button onClick={() => startGame('space')}>
                    &gt; Space
                </button>

                <button onClick={() => startGame('medieval')}>
                    &gt; Medieval
                </button>

                <button onClick={() => startGame('cyberpunk')}>
                    &gt; Cyberpunk
                </button>
            </div>

            <div className="login-actions">
                <button onClick={onLogout}>
                    &gt; Log Out
                </button>
            </div>

            <Scoreboard />
        </div>
    )
}

export default ThemeSelection