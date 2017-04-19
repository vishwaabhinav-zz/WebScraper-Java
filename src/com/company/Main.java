package com.company;

public class Main {

    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Integer limit = 1;
            if(args.length >= 2) {
                limit = Integer.valueOf(args[1]);
            }
            Scraper.init(args[0], limit);
        } catch(Exception e) {
            System.out.println("Oops!!");
            e.printStackTrace();
        } finally {
            System.out.println(Scraper.getOutput());
            System.out.println("Errors ------------");
            System.out.println(Scraper.getError());
            double time = ((System.nanoTime() - start) / 1000000000D);
            System.out.println("Time taken : " + time + " sec");
        }
    }
}
