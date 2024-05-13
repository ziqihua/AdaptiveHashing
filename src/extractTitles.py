import pandas as pd

def export_movie_titles(csv_file_path, output_file_path):
    # Load the CSV file
    df = pd.read_csv(csv_file_path)

    # Assuming the column storing movie titles is named 'names'
    movie_titles = df['names'].dropna().unique()  # Drop any missing values and get unique titles

    # Save to a text file, each title on a new line
    with open(output_file_path, 'w') as file:
        for title in movie_titles:
            file.write(title + '\n')

# Example usage
export_movie_titles('imdb_movies_rev copy.csv', 'movie_titles.txt')
