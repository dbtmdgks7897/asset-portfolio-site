# My Asset Manager — progress

## Where things stand (2026-10-10)

The owner decided to keep this project as an archived 2023 snapshot. It will not be revived.

**Local commits (not pushed):**
- `262c2fc`: fixes the two `data.sql` statements that blocked a clean boot.
  - Removed the duplicate admin insert (idx 1).
  - Added the missing `;` after the `PORTFOLIO` insert.
  - Verified: the app boots on H2 with the committed seed.
  - Side effect: `ROLE_ADMIN` moved to mock seed user 1. This was corrected in the third commit.
- `375f512`: snapshot README (Korean first, with a short Japanese section), the review, this file, and 4 screenshots in `docs/screenshots/2026-10-10/`.
- Third commit: removes personal data from the seed.
  - The owner's real name in user 101 is now `데모유저`.
  - That user's BCrypt hash is now the hash of `demo1234`.
  - `USER_ROLES` now points at user 101.
  - The demo login is `w@w.c` / `demo1234` (admin), documented in the README.
  - **The old values are still in git history.**

**Unchanged:**
- All other code and seed data.
- The other 101 mock seed users (generated names and emails, their own hashes).
- The 4 API keys still in public git history (commit `e74657f`).

The full findings are in `docs/review-2026-10-10.md`.

## Next

- [x] Full review → docs/review-2026-10-10.md
- [x] Owner decision: archive snapshot (no revival)
- [x] Fix the 2 boot-blocking `data.sql` lines (local commit)
- [x] Rewrite README as an honest snapshot record (KO + JA), with 4 screenshots
- [ ] Push to origin. The command post does this after the owner approves.
- [ ] Owner: revoke or rotate the 4 leaked keys (KIS appkey/secret, data.go.kr, AlphaVantage), or confirm the accounts are closed
- [x] Replace the real name and owner hash in `data.sql` with demo values (local commit)
- [ ] Owner: decide whether to rewrite git history. The old name and hash, and the 4 API keys, are still there. Rewriting needs a force-push.
- [ ] Optional: archive the GitHub repo (read-only) after pushing
- [ ] imysh project page from the README, screenshots and demo videos (portfolio-page-generator, done in imysh)

## Log

- 2026-10-10: Cleaned personal data from the seed: real name → 데모유저, owner hash → `demo1234`, admin role → user 101. Verified on H2 and updated the README demo login, the review, and this file. Local commit only; the old values are still in history.
- 2026-10-10: Owner chose archive. Committed locally (not pushed): the 2-line `data.sql` boot fix, then the snapshot README (KO/JA) with 4 screenshots, review, and progress. Moved the other 22 screenshots out of the repo.
- 2026-10-10: Reviewed the whole repo: built it, booted it on H2, called the APIs live, probed the external market-data sources, took 26 screenshots, and checked git history for secrets. Wrote docs/review-2026-10-10.md.
- 2026-10-10: Created progress file.
