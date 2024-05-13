Adaptive Hashing System

Overview-

The Adaptive Hashing System is designed to dynamically select the most efficient hashing technique or filtering method based on real-time performance metrics. This system integrates multiple hashing strategies and filters, including Double Hashing, Linear Probing, Linear Probing with Steps, Quadratic Probing, and Pseudo-Random Probing, along with Bloom and Cuckoo Filters, to provide optimized data handling capabilities for diverse datasets.

Features-

Dynamic Hashing Techniques: Double Hashing, Linear Probing, Linear Probing with Steps, Quadratic Probing, Pseudo-Random Probing

Efficient Data Filters: Bloom Filter, Cuckoo Filter

Performance Benchmarking: 

1. Preload data and measure performance metrics such as insertion time, search time, and deletion time.
2. Dynamically switch between hashing techniques and filters based on operational performance.

Usage-

After starting the application, in "main", use the command-line interface to interact with the system:

1. Insert a title: insert <movie_title>
2. Search if a title is present: search <movie_title>
3. Delete a title: delete <movie_title>
4. Compare performance metrics: compare
5. Exit the program: exit
