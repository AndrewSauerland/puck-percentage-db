# The purpose of this repository is to **automatically** update the puck percentage database

### This uses aws CodePipeline and EventBridge Scheduler to fun a job nightly at 3am

*First*, csv files are downloaded for skater and goalie stats

*Second*, a series of file readers and database connections transfer all cells into the database

*Third*, names are adjusted as-needed to match previous sheets and current lineups

*Finally*, the date is added in an entry to the tables to act as a confirmation for upload
