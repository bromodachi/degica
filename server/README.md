## Degica Ledger project


# Note
This was primarily worked only on a Sunday and Tuesday night(2 days) on both the frontend and backend.
I had to take some shortcuts and write code that's not really pretty. They may be some oversights or bugs in the application.
Also please note that I'm primarily a backend developer so frontend is not my strong point

The following assumptions are made:
- We are only supporting JPY, KRW, and USD.
  This is done to basically have the same currencies as the one in figma and to simplify things. There are hundreds of currencies and to support all in a small project is not feasible unless we use a third party gateway
- Lack of git commits. Ideally, each request was its own branch and we build upon it. However, since I went to request 1 and then request 3, I just merged all of them. Can't really reflect my work style as this was rushed in 2 days.
- Testing is just restricted to trophy testing. Due to the limit of time, I had to skip unit tests.


## How to run the program

Usually, I write supply a dockerfile so all you have to do is run docker-compose up but due to lack of time, please allow me to omit this. Instead, please have docker running in the background. If you have a mac, all you have to do is install it via brew:

```
brew install docker
```

You need a postgres image.
```
docker pull postgres
```

Then run it in the background:
```
docker run --name postgres --publish 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```

Next, run flyway. Unfortunately, we have to use an older version of flyway since the newer one is having problems with postgres.

```
cd server
./gradlew flywayClean && ./gradlew flywayMigrate
```

If that succeeds, all you have to do is run:
```
./gradlew bootRun
```

For the frontend, you should install everything in the package:
```
cd ../frontend
npm install
```
Once that succeeds, you can just start the application.
```
npm start
```

### Tech used
 - I was about to code in rust and vue.js however spring is mature and has multiple mature libraries. Specifically, test containers. Test containers enables you to use containers within your test cases. Spring is also easy to write something up real quick
 - Postgres -> Postgres is a more robust database than mysql. 
 - React -> Personally, just wanted to learn this.


### Shortcomings and how I would improve the project

Due to some personal commitments, I only had 2 days to work on this. There are many aspects I would like to improve if I had a few more additional day:
- Supply a dockerfile. Ideally, the steps above shouldn't have to be done manually. It might have been better to even supply a shell script to at least do the above.
- More integration and unit tests. Unfortunately, the frontend took a bit more time as I had to research how to use react and piece them together. I had to sacrifice some time in the backend to fix up things in the frontend
- Remove hardcoding of urls and put them in a config file. This is for both the frontend and backend. Currently, localhost is hardcoded and we shouldn't be doing that.
- When adding a new item, the frontend always goes back to the front page(even if you add a later date). I only did this to make it easier to update the list view.
- In the backend, we currently always retrieve the entries and do the calculation of the total amount. Ideally, we shouldn't be doing this. Instead, every time we add an entry, simply have a field in ledger that keep tracks of the amount and update it there when adding the entry(this will be in one transaction). However, I noticed this late and to save time, just retrieved all the entries.
- We should split the components more to allow more reusable views. At first, this was my goal as I had a directory called components but had to rush the frontend.
- Better error handling 
