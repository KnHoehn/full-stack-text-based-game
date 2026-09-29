// Represents the game state data returned by the game API to the frontend.
export type GameResponse = {
    gameId: string
    gameName: string
    story: string
    currentRoom: string
    itemDescription: string | null
    inventory: string[]
    gameOver: boolean
    movementMessage: string
    message: string
    score: number
    moves: number
    time: number
}