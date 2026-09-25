export type GameResponse = {
    gameId: string
    gameName: string
    story: string
    currentRoom: string
    itemDescription: string | null
    inventory: string[]
    gameOver: boolean
    message: string
}