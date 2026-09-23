import type { GameState } from './types/GameState'

type ThemeSelectionProps = {
    onGameStarted: (gameState: GameState) => void
}

function ThemeSelection({ onGameStarted }: ThemeSelectionProps) {

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

            const gameState: GameState = await response.json()

            onGameStarted(gameState)

        } catch (error) {
            console.error(error)
        }
    }


    return (
        <div>
            <h2>Choose Your Game Theme</h2>

            <button onClick={() => startGame('space')}>
                Space
            </button>

            <button onClick={() => startGame('medieval')}>
                Medieval
            </button>

            <button onClick={() => startGame('cyberpunk')}>
                Cyberpunk
            </button>
        </div>
    )
}

export default ThemeSelection