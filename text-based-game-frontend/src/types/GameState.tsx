export type GameState = {
    gameId: string
    theme: string
    userName: string
    currentRoom: string
    inventory: string[]
    moves: number
    moveScore: number
    startTime: number
    gameOver: boolean
}