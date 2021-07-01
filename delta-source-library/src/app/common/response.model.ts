import {BookModel} from "./book.model";

export interface ResponseModel {
  content: BookModel[];
  first: boolean;
  last: boolean;
  pageable: {
    pageNumber: number;
  }
}
