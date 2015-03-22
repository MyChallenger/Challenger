package com.example.vikramjeet.challengerapp;

import com.example.vikramjeet.challengerapp.models.Comment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class CommentTest extends ChallengerTestCase {

    public void testAdd() {
        final Comment comment = new Comment();
        comment.setText("This is a test comment!");
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                assertNull(e);
                comment.fetchInBackground(new GetCallback<Comment>() {
                    @Override
                    public void done(Comment commentWithAuthor, ParseException e) {
                        assertNotNull(commentWithAuthor.getAuthor());
                        countDown();
                    }
                });
            }
        });
        await();
    }
}

