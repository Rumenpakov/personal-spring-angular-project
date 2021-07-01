export interface BookModel {
  book: {
    title: string,
    shortSummary: string,
    imgUrl: string;
    authors: {
      name: {
        name: string;
      };
    }[]
    categories: string[];
  }
  isbn: string;
}
