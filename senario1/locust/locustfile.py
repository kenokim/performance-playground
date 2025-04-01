from locust import HttpUser, task, between
from datetime import datetime, timedelta
import random

class OrderUser(HttpUser):
    wait_time = between(1, 3)

    @task
    def get_orders(self):
        # Generate random user ID between 1 and 100
        user_id = random.randint(1, 100)
        
        # Generate random date range for the last 30 days
        end_date = datetime.now()
        start_date = end_date - timedelta(days=30)
        
        # Format dates for the API
        start_date_str = start_date.strftime("%Y-%m-%dT%H:%M:%S")
        end_date_str = end_date.strftime("%Y-%m-%dT%H:%M:%S")
        
        # Make the API request
        self.client.get(
            f"/api/orders?userId={user_id}&startDate={start_date_str}&endDate={end_date_str}",
            name="Get Orders"
        ) 