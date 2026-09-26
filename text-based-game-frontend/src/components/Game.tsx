import { useState } from 'react'
import type { GameResponse } from '../types/GameResponse.tsx'
import formatTime from '../utils/formatTime'

type GameProps = {
    gameResponse: GameResponse
    onGameExit: () => void
}


function Game({ gameResponse, onGameExit }: GameProps) {

    const [gameState, setGameState] = useState(gameResponse)
    const [command, setCommand] = useState('')
    const [showInstructions, setShowInstructions] = useState(true)

    async function handleCommand() {
        if (command.trim() === '') {
            return
        }

        try {
            const token = localStorage.getItem('token')

            const response = await fetch(
                `/api/games/${gameResponse.gameId}/command`,
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        command: command
                    })
                }
            )

            if (!response.ok) {
                console.error('Unable to process command')
                return
            }

            const result: GameResponse = await response.json()

            if (result.message === 'instructions') {
                setShowInstructions(true)
                setGameState({
                    ...result,
                    message: ''
                })
            } else {
                setShowInstructions(false)
                setGameState(result)
            }

            setCommand('')

        } catch (error) {
            console.error(error)
        }
    }

    return (
        <div>
            <h2>{gameState.gameName}</h2>

            <p>{gameState.story}</p>

            {showInstructions && (
                <>
                    <h3>Instructions</h3>

                    <p>Movement commands: Go North, Go South, Go East, Go West</p>
                    <p>Add to inventory: Get &lt;item name&gt;</p>
                    <p>Type 'Exit' to exit game</p>
                    <p>Type 'I' to show instructions again</p>
                </>
            )}

            <p style={{ whiteSpace: 'pre-line' }}>{gameState.message}</p>

            {!gameState.gameOver && (
                <p>You are in the {gameState.currentRoom}.</p>
            )}

            {gameState.gameOver && (
                <>
                    <p>Score: {gameState.score}</p>
                    <p>Total Moves: {gameState.moves}</p>
                    <p>Total Time: {formatTime(gameState.time)}</p>
                </>
            )}

            {!gameState.gameOver && gameState.itemDescription && (
                <p>{gameState.itemDescription}</p>
            )}

            {!gameState.gameOver && (
                <>
                    <h3>Inventory</h3>

                    {gameState.inventory.length === 0 ? (
                        <p>Your inventory is empty.</p>
                    ) : (
                        <ul>
                            {gameState.inventory.map((item) => (
                                <li key={item}>{item}</li>
                            ))}
                        </ul>
                    )}
                </>
            )}

            <div className="command-line">

                {!gameState.gameOver && (
                    <input
                        type="text"
                        value={command}
                        onChange={(event) => setCommand(event.target.value)}
                        onKeyDown={(event) => {
                            if (event.key === 'Enter') {
                                handleCommand()
                            }
                        }}
                        autoFocus
                    />
                )}

                {gameState.gameOver && (
                    <button onClick={onGameExit}>
                        &gt; Exit
                    </button>
                )}
            </div>
        </div>
    )
}

export default Game