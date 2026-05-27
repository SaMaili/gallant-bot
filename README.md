# Gallant Bot

A Discord bot built for the **Gallant** Brawlhalla clan family. Automatically tracks and posts clan member stats from the Brawlhalla API, manages clan battle schedules, and provides various utility commands. Ran 24/7 as a Heroku worker dyno.

## ⚠️ Disclaimer

**This project is no longer maintained.** The Gallant clan has disbanded/is inactive and the bot has not been running since.

### Source Code
The original source code was lost — only the compiled `Gallant.jar` remains in this repository. To document the project, the JAR was decompiled using an AI-assisted tool (CFR decompiler + Claude) in May 2026. The Code itself **IS NOT Vibecoded** since AI wan't a thing back then. This README was written entirely based on that decompiled output, not from the original source. The decompiled code may differ slightly from what was originally written, but accurately reflects the bot's functionality.

### Brawlhalla API
The bot uses the **legacy Brawlhalla API** (`api.brawlhalla.com`), which requires an API key. Blue Mammoth Games has since released a [new public API](https://dev.brawlhalla.com) that requires no authentication. The legacy endpoint the bot depends on will be **shut down at the end of 2026**, after which the leaderboard and update features will stop working entirely without a migration to the new API.

---
## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 8 |
| Discord API | [JDA 4.2.0](https://github.com/DV8FromTheWorld/JDA) |
| Database | SQLite 3.34 via JDBC |
| External API | Brawlhalla API |
| Encoding | Nashorn JS engine + custom UTF-8 decoder (handles CJK player names) |
| Deployment | Heroku (Worker dyno) |

## Setup

### Prerequisites
- Java 8+
- A Discord bot token
- A Brawlhalla API key (legacy endpoint — valid until end of 2026; the [new API](https://dev.brawlhalla.com) no longer requires a key)

### Environment Variables

| Variable | Description |
|---|---|
| `TOKEN` | Discord bot token |
| `BH_API_KEY` | Brawlhalla API key |

### Run

```bash
java -jar Gallant.jar
```

The bot creates `data.db` on first run and persists all configuration (channels, ELO/XP requirements) across restarts.

### Heroku

```
Worker: java -jar Gallant.jar
```

## Database Schema

| Table | Description |
|---|---|
| `channels` | Stores configured channel IDs per guild (leaderboard, CB reminder, rankers) |
| `eloRequirements` | Per-clan ELO join requirements |
| `xpRequirements` | Per-clan minimum average daily XP |
| `cbTeams` | Clan battle team rosters |


## Features

### Automated Tasks
- **Leaderboard update** — every 6 hours (00:00, 06:00, 12:00, 18:00 CET), fetches all clan members from the Brawlhalla API and posts a color-coded stat table to a configured channel
- **Clan battle reminder** — daily at 06:00 CET, pings the day's CB organizer with a pre-filled battle announcement template (lineup, ELO range, date)
- **Counting channel** — monitors a counting channel and automatically posts the next number on milestones (multiples of 1111, numbers ending in 69 / 420, 8008) or randomly (~10% chance)

### Commands
All commands use the prefix `.gl`.

| Command | Description | Permission |
|---|---|---|
| `help` | Shows all available commands | Everyone |
| `grab` | Picks a random server member with a randomized message | Everyone |
| `maps` | Posts the Gallant mod pack download link | Everyone |
| `update <1\|2\|3\|4\|all\|rules>` | Manually triggers a leaderboard update for one or all clans | Staff |
| `disable <1\|2\|3\|4>` | Toggles a clan on/off for leaderboard updates | Staff |
| `set lbc` | Sets the leaderboard channel to the current channel | Staff |
| `set remindcb` | Sets the clan battle reminder channel | Staff |
| `set rankers` | Sets the rankers channel | Staff |
| `set eloreq <clan> <elo>` | Sets the ELO requirement for a clan (min. 750) | Staff |
| `set xpreq <clan> <xp>` | Sets the XP requirement for a clan | Staff |
| `cb teams show` | Displays the clan battle team rankings (Gladiators / Knights / Nobles) | Staff |
| `info` | Prints current bot config and channel info to console | Everyone |
| `shutdown` | Gracefully shuts down the bot and sends the database file to Discord | Creator |

### Leaderboard Format

The leaderboard is posted as a `diff`-formatted code block with four member states:

```diff
+  PlayerName        days_in_clan    avg_daily_xp    peak_elo    ← meets both ELO and XP requirement
-  PlayerName        days_in_clan    avg_daily_xp    peak_elo    ← meets neither
   PlayerName        days_in_clan    avg_daily_xp    peak_elo    ← meets ELO only
---PlayerName        days_in_clan    avg_daily_xp    peak_elo    ← meets XP only
```

Members in the clan for fewer than 7 days are exempt from the XP requirement.

### Clans

| # | Name | Brawlhalla Clan ID |
|---|---|---|
| 1 | Gallant | 526412 |
| 2 | Gallant II | 1108627 |
| 3 | Gallant Valiant | 1152725 |
| 4 | Gallant NA | 1269820 |

## Context

Built in 2021 as a hobby project for the Gallant Brawlhalla clan. Actively developed over ~12 months (79 commits) and ran live on Heroku for the clan's lifetime.

---

*Made by SaMaili for the Gallant clan ❤️ it was a great time*
