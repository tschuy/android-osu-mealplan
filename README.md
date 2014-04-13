OSU Mealplan Tracker
====================

Access Oregon State mealplan balance through a convenient Android app. Future features:

   - Cards UI for mealplan balance, Orange Rewards
   - Log-in using Android-styled user/pass box
       - Download login page from http://myuhds.oregonstate.edu
       - Extract *lt* value (see <div class="form-actions">)
       - Send HTTP Post, receive cookie in response and store
       - Download and parse information page
   - Notifications:
       - Notification Frequency:
           - Daily
           - Weekly
           - Off
       - "Meals per Day": get user preference for number of meals expected
       - Display:
           - Mini information:
               - $xxx.xx remaining
               - $xxx.xx per day
               - Time notification was created
           - Expanded:
               - All of above, plus
               - Orange Rewards: $xxx.xx
   - If mealplan is lower than Orange Rewards, prioritize Orange Rewards (reverse order in main screen, use in mini information)