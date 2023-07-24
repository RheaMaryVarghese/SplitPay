from fastapi import FastAPI,HTTPException
from sqlalchemy import create_engine,text,Table, Column, Integer, Float, String,MetaData
from sqlalchemy.orm import sessionmaker
from pydantic import BaseModel
from typing import List




# Create an instance of the FastAPI app
app = FastAPI()

# Configure the database connection
DATABASE_URL = "mysql://root:sql2022#@localhost/info"  # Replace with your MySQL connection details

# Create the database engine
engine = create_engine(DATABASE_URL)


# Create a SessionLocal class
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

metadata=MetaData

# API endpoint for sign-up
@app.post("/signup")
def signup(user: dict):
    db = SessionLocal()

    raw_query = text(
        'INSERT INTO user_info (Name, Age, Gender,Email_id,Phone_no,Pic_url,Username,Password) VALUES (:Name, :Age, :Gender, :Email_id, :Phone_no, :Pic_url, :Username, :Password)'
        )
    db.execute(raw_query,user)
    db.commit()


    raw_query1=text(f"CREATE TABLE IF NOT EXISTS transactions_{user.get('Name')} (Name VARCHAR(50) NOT NULL, Amount FLOAT NOT NULL)")
    db.execute(raw_query1)
    db.commit()

    raw_query2=text(f"CREATE TABLE IF NOT EXISTS settled_transactions_{user.get('Name')} (Name VARCHAR(50) NOT NULL, Amount FLOAT NOT NULL)")
    db.execute(raw_query2)
    db.commit()
    
    return user





# Implement the login endpoint
@app.post("/login")
def login(request: dict):
    # Retrieve the username and password from the request
    username = request.get("username")
    password = request.get("password")



    # Example authentication logic using raw SQL
    db = SessionLocal()

    raw_query = text('SELECT * FROM user_info WHERE Username=:username AND Password=:password')
    result = db.execute(raw_query,{"username": username, "password": password})
     
    user = result.fetchone()

    if user:
        # If user details found, return them as JSON
        return {
            "Name": user[0],
            "Age": user[1],
            "Gender": user[2],
            "Email_id": user[3],
            "Phone_no": user[4],
            "Pic_url": user[5],
        }
    else:
        # If user details not found, raise an HTTPException with 401 Unauthorized status code
        raise HTTPException(status_code=401, detail="Invalid username or password") 


#To get names of all users
@app.get("/users")
def get_users():
    db = SessionLocal()
    raw_query = text("SELECT Name FROM user_info")
    result = db.execute(raw_query)
    users = [{"Name": row[0]} for row in result]
    return users





@app.post("/store_split/{table_name}")



# Define a function to dynamically create the table based on the obtained name
def create_table(table_name,split_data: List[dict]):
    db = SessionLocal()
    raw_query = text(f"CREATE TABLE IF NOT EXISTS {table_name} (user_name VARCHAR(50) NOT NULL, amount_to_pay FLOAT NOT NULL)")
    db.execute(raw_query)
    db.commit()

    db = SessionLocal()
    raw_query=text("INSERT INTO splits (Name) VALUES (:Name)")
    db.execute(raw_query, {"Name": table_name})
    db.commit()
    
    db = SessionLocal()
    for data in split_data:
            user_name = data.get("user_name")
            amount_to_pay = data.get("amount_to_pay")

            # Perform additional validation or data processing as needed
            raw_query = text(f"INSERT INTO {table_name} (user_name, amount_to_pay) VALUES (:user_name, :amount_to_pay)")
            db.execute(raw_query,
                       {"user_name": user_name, "amount_to_pay": amount_to_pay})

    db.commit()
    return {"message": "Payment data stored successfully."}


# API endpoint to get transaction data
@app.get("/transactions")
def get_transactions():
    db = SessionLocal()
    raw_query = text("SELECT Name, Amount FROM transaction")
    result=db.execute(raw_query)
    transactions = [{"userName": row[0], "amount": row[1]} for row in result]
    return transactions




@app.post("/settle_transaction/{user_name}")
def settle_transaction_by_name(user_name: str):
     db = SessionLocal()
     raw_query = text("SELECT * FROM transaction WHERE Name=:user_name")
     result = db.execute(raw_query, {"user_name": user_name})
     row = result.fetchone()
     db.commit()


     raw_query2 = text(f"INSERT INTO settled_transactions (Name, Amount) VALUES (:Name, :Amount)")
     db.execute(raw_query2, {"Name": row[0], "Amount": row[1]})
     db.commit()


     raw_query3 = text(f"DELETE FROM transaction WHERE Name=:user_name")
     db.execute(raw_query3, {"user_name": user_name})
     db.commit()



@app.post("/lent_transaction")
def add_lent_transaction(data:dict):
       
        db = SessionLocal()
        Name=data.get("name")
        Amount=data.get("amount")

        # Prepare the raw SQL query to insert data into the table
        insert_query = text("INSERT INTO transaction (Name, Amount) VALUES (:Name, :Amount)")

        # Execute the query
        db.execute(insert_query, {"Name": Name, "Amount": Amount})

        # Commit the changes to the database
        db.commit()

        return {"message": "Lent transaction added successfully."}







# Run the FastAPI application with uvicorn
if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app,host="localhost",port=8000)
