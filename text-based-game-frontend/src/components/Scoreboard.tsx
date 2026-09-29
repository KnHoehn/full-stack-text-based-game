import { useEffect, useState } from 'react'
import formatTime from '../utils/formatTime'

function formatTheme(theme: string) {
    return theme.split(' ')[0]
}

type Score = {
    id: number
    userName: string
    score: number
    moves: number
    time: number
    theme: string
}

function Scoreboard() {

    const [scores, setScores] = useState<Score[]>([])
    const [leaderboard, setLeaderboard] = useState<Score[]>([])

    useEffect(() => {
        async function getScores() {
            try {
                const token = localStorage.getItem('token')

                const response = await fetch('/api/scores/me', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                })

                if (!response.ok) {
                    console.error('Unable to retrieve scores')
                    return
                }

                const result: Score[] = await response.json()

                setScores(result)

                const leaderboardResponse = await fetch('/api/leaderboard', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                })

                if (!leaderboardResponse.ok) {
                    console.error('Unable to retrieve leaderboard')
                    return
                }

                const leaderboardResult: Score[] = await leaderboardResponse.json()

                setLeaderboard(leaderboardResult)

            } catch (error) {
                console.error(error)
            }
        }

        void getScores()
    }, [])

    return (
        <div>
            <h2>Your Top Ten Scores</h2>

            {scores.length === 0 ? (
                <p>No scores yet.</p>
            ) : (
                <table className="score-table">
                    <thead>
                    <tr>
                        <th>Rank</th>
                        <th>Theme</th>
                        <th>Score</th>
                        <th>Moves</th>
                        <th>Time</th>
                    </tr>
                    </thead>

                    <tbody>
                    {scores.map((score, index) => (
                        <tr key={score.id}>
                            <td>{index + 1}</td>
                            <td>{formatTheme(score.theme)}</td>
                            <td>{score.score}</td>
                            <td>{score.moves}</td>
                            <td>{formatTime(score.time)}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            <h2>Global Leaderboard</h2>

            {leaderboard.length === 0 ? (
                <p>No scores yet.</p>
            ) : (
                <table className="score-table">
                    <thead>
                    <tr>
                        <th>Rank</th>
                        <th>Player</th>
                        <th>Theme</th>
                        <th>Score</th>
                        <th>Moves</th>
                        <th>Time</th>
                    </tr>
                    </thead>

                    <tbody>
                    {leaderboard.map((score, index) => (
                        <tr key={score.id}>
                            <td>{index + 1}</td>
                            <td>{score.userName}</td>
                            <td>{formatTheme(score.theme)}</td>
                            <td>{score.score}</td>
                            <td>{score.moves}</td>
                            <td>{formatTime(score.time)}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    )
}

export default Scoreboard