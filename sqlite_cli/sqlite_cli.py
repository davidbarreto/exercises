import sqlite3
import sys
import traceback
from tabulate import tabulate

"""
Simple SQLite query executor for Python
Implemented in a dark moment where I didn't have any
tools available, and not able to download anything :(
"""

def script_mode(sqlite_db_file, script_list):
    
    for script in script_list:
        conn = sqlite3.connect(sqlite_db_file)
        c = conn.cursor()
        c.executescript(script)
        conn.commit()
        conn.close()
        
    print("Scripts executed sucessfully")
    
def interactive_mode(sqlite_db_file=":memory:"):
    
    print("====== Welcome to the SQLite CLI =======")
    
    while True:
        
        conn = sqlite3.connect(sqlite_db_file)
        conn.row_factory = sqlite3.Row
        c = conn.cursor()
        sql = ""
        
        while True:
            try:
                command = input("> ")
            except KeyboardInterrupt:
                print("Restarting buffer... to quit, type: quit")
                sql = ""
                continue
            
            if command == "quit":
                return
            
            sql += " " + command
            if command.endswith(";"):
                break
        
        print("sql: ", sql)
        try:
            execute_sql(sql, conn)
        except:
            print(f"Error executing sql [{sql}]: " + traceback.format_exc())
            print("Please try again")
            
        conn.close()
        
def execute_sql(sql, conn):
    
    cur = conn.cursor()
    cur.execute(sql)
    

    if cur.row_count >= 0:
        print("Affected rows: ", cur.rowcount)
    else:
        format_result(cur.fetchall())
        
    conn.commit()

def format_result(records):
    
    if len(records) > 0:
        print(tabulate(records, headers=records[0].keys(), tablefmt="psql"))
    else:
        print("No records found")
        
def read_script(file_path):
    
    with open(file_path, "r") as file:
        script = file.read()
    
    return script
    
def show_help():
    help_text = """
====== SQLite CLI Help ======

USAGE:
    python sqlite_cli.py <database_file> [script_file] [script_file] ...

ARGUMENTS:
    <database_file>     Path to the SQLite database file (required)
                        Use ':memory:' for an in-memory database
    
    [script_file]       Optional SQL script files to execute
                        Multiple script files can be provided

MODES:
    
    1. INTERACTIVE MODE (no script files provided):
       - Launches an interactive SQL query shell
       - Enter SQL commands ending with semicolon (;)
       - Type 'quit' to exit the interactive session
       - Press Ctrl+C to restart the buffer without clearing history
    
    2. SCRIPT MODE (script files provided):
       - Executes the provided SQL script files
       - Scripts are executed in the order they are provided
       - The program exits after all scripts complete

EXAMPLES:

    1. Interactive mode with database file:
       python sqlite_cli.py mydb.db
    
    2. Interactive mode with in-memory database:
       python sqlite_cli.py :memory:
    
    3. Execute a single script:
       python sqlite_cli.py mydb.db init.sql
    
    4. Execute multiple scripts in sequence:
       python sqlite_cli.py mydb.db schema.sql data.sql

INTERACTIVE COMMANDS:
    
    > SELECT * FROM users;
    - Displays query results in a formatted table
    
    > INSERT INTO users VALUES (1, 'John');
    - Executes the insert and shows affected rows
    
    quit
    - Exits the interactive session
    
    Ctrl+C
    - Clears the current buffer and starts fresh

"""
    print(help_text)
    
def build_script_list(args):
    
    if len(args) < 3:
        return None
    return args[2:]

def main():
    
    if len(sys.argv) < 1:
        print(f"Wrong number of arguments: {(len(sys.argv) -1)}", file=sys.stderr)
        show_help()
        exit(1)
        
    sqlite_db_file = sys.argv[1]
    script_list = build_script_list(sys.argv)
    
    if script_list:
        script_mode(sqlite_db_file, script_list)    
    else:
        interactive_mode(sqlite_db_file)
        
    print("Bye")
    
if __name__ == "__main__":
    main()
    
            


        
    