# U-Fund — Produce Relief for the Homeless & Weather-Affected in Tallahassee, FL

A full-stack web app that helps coordinate and fund essential produce and supplies for people experiencing homelessness and those impacted by severe weather in Tallahassee, Florida.

- **Live site:** https://bdweiler.github.io/UFund-261-/

---

## Team

- Brock Weiler  
- Daniel Catorcini  
- Alec Vuillermoz  
- Matt Spring

---

## What It Does

- Browse a catalog of community **needs** (produce & essentials)  
- Add items to a **basket** and adjust quantities  
- **Checkout** to allocate funds/items and decrement inventory  
- View the **leaderboard** of top contributors

---

## Tech Stack

**Frontend**
- Angular (TypeScript SPA)
- Hosted on **GitHub Pages**

**Backend**
- Java **17**, Spring Boot (REST API)
- Maven build
- Hosted on **Railway**

---

## How to Use (Testers & Demo)

1. Open the live site and **log in** (use your test/demo credentials).  
2. Visit **Needs** to browse items; add selected items to your **Basket**.  
3. Adjust quantities in **Basket** and **Checkout** when ready.  
4. Open **Leaderboard** to see aggregate contributions.

> If data fails to load, ensure the API is reachable at https://ufund-261-production.up.railway.app/needs 

---

## API Overview

> High-level summary of endpoints used by the UI. Check controller annotations for the authoritative list.

### Needs
- `GET /needs` — list all needs  
- `GET /needs/{id}` — fetch a specific need  
- `POST /needs` — create a need  
- `PUT /needs/{id}` — update a need  
- `DELETE /needs/{id}` — delete a need  

### Users *(example; align with actual mappings)*
- `GET /users?username={username}` — search by username  
- `PUT /users/{id}` — update user (e.g., basket contents, totals)
---

## License

MIT License

See LICENSE for details.
