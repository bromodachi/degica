# Process

1. Unzip the file
2. Complete the requests below
3. Zip up the solution (make sure to include the .git folder!)
4. Send the zip file back to us as instructed in our email

At Degica we use mostly vue.js, but please use whichever language and framework you like and feel the most comfortable with.
Please use the `README.md` file to add notes of any design decisions, trade-offs, or improvements you'd make. If there is something you really want to share with us but you feel it might be seen as over-engineering, that is fine. We are engineers too, we love code. Keeping the application small and simple is also perfectly fine. Do not forget to add appropriate test code as well.

Later on, in the interview process you may be asked to walk us through the code so please have that in mind for your next steps.

# Context

The company wants to develop an internal ledger to track internal incomes and expenses.

We then began developing a ledger system to reach that goal.

# Before you begin

Each following feature requests are purposedly generic, it will be your choice to define what is acceptable and satisfactory.

# Request 1

You are given the following wireframes from and asked to implement the first version of the ledger:

* [Figma Wireframe](https://www.figma.com/file/nKWXLyJd1kxMC0AnPGiWHl/Untitled?node-id=0%3A1)

The figma file above is just a wireframe, it is not the final design, so feel free to style the page however you like. You can write your own css or use a frontend framework, be minimalist or go fancy.

Data can be retrieved from this URL: [https://take-home-test-api.herokuapp.com/invoices](https://take-home-test-api.herokuapp.com/invoices)

Note that this API endpoint might be a little flaky at times.

# Request 2

Accounting now would like to be able to understand the current status of the ledger.
Add the computation logic of the ledger total amount and display it on the FE (Where to display that info is left to you and your sense of UI/UX)

# Request 3

For reporting reasons, accounting would like to be able to export the ledger in CSV format,
add the ability to allow us to download that file (generation logic is left to you).

# Request 4

Our stakeholders want to be able to add entries into a new ledger.
Implement a solution (DB, Backend, Frontend included), placement and design is left up to you.

**Note:** These ledger entries do not need to be sent back to the server
