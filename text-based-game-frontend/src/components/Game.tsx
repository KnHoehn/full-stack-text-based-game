import type { GameState } from './types/GameState'

type GameProps = {
    gameState: GameState
}

function Game({ gameState }: GameProps) {
    return (
        <div>
            <h2>{gameState.theme} Adventure</h2>

            <p>Player: {gameState.userName}</p>

            <p>Current Room: {gameState.currentRoom}</p>

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
        </div>
    )
}

export default Game