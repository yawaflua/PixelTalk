# PixelTalk

PixelTalk is a comprehensive socialization plugin for Minecraft teenagers, designed to encourage friendly interactions and maintain a healthy, moderated environment.

## Features
- **Questionnaires**: New players must complete a profile setup on their first join (Preferred Language, Interests, Age 6-18).
- **Points System**: Players are assigned social points.
  - Negative actions (e.g., PvP, Profanity) result in automatic deduction of points.
  - If a player's points drop to 0, they receive an automatic temporary ban.
- **Reporting System**: Integrated `/report` command for players to alert moderators of misconduct.
- **PlasmoVoice Integration**: Automatically dumps audio recordings when a report is filed (optional).
- **MongoDB Storage**: Profiles, scores, and data are securely stored using MongoDB.

## Installation
1. Drop the compiled `PixelTalk.jar` into your server's `plugins` folder.
2. Configure your MongoDB connection and messages in `config.yml`.
3. (Optional) Install `PlasmoVoice` on your server for voice chat & audio dumping features. 
4. Start the server!

---

# PixelTalk (עברית)

PixelTalk הוא תוסף (פלאגין) סוציאליזציה מקיף לשרתי מיינקראפט לבני נוער, שנועד לעודד אינטראקציות חברתיות חיוביות ולשמור על סביבה בטוחה ומפוקחת.

## תכונות מרכזיות
- **שאלון היכרות**: שחקנים חדשים נדרשים למלא פרופיל אישי בהתחברותם הראשונה (שפה מועדפת, תחומי עניין וגיל 6-18).
- **מערכת נקודות**: לכל שחקן יש נקודות חברתיות.
  - פעולות שליליות לשחקנים אחרים (כגון קללות, או תקיפה ב-PvP) מפחיתות נקודות באופן אוטומטי.
  - שחקן שמגיע ל-0 נקודות יקבל חסימה (Ban) זמנית מהשרת באופן אוטומטי.
- **מערכת דיווחים**: פקודת `/report` מאפשרת לשחקנים לדווח לצוות השרת על התנהגות בלתי הולמת.
- **תמיכה ב-PlasmoVoice (אופציונלי)**: במקרה של דיווח, הפלאגין יודע לשמור באופן אוטומטי הקלטות שמע לגיבוי.
- **אחסון MongoDB**: כל נתוני הפרופילים והניקוד נשמרים בצורה יעילה ומאובטחת במסד נתונים מסוג MongoDB.

## התקנה מומלצת
1. הוסיפו את הקובץ `PixelTalk.jar` המקומפל לתיקיית ה-`plugins` בשרת שלכם.
2. הגדירו את חיבור ה-MongoDB ושאר ההגדרות בקובץ `config.yml`.
3. (אופציונלי) התקינו את פלאגין ה-`PlasmoVoice` בשרת שלכם לתמיכה בניהול צ'אט קולי והקלטות שמע.
4. הפעילו את השרת!
