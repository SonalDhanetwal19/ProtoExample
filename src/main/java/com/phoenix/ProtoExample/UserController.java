package com.phoenix.ProtoExample;

import com.google.protobuf.InvalidProtocolBufferException;
import com.phoenix.ProtoExample.UserProto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping(consumes = "application/x-protobuf", produces="application/x-protobuf")
    public ResponseEntity<byte[]> createUser(@RequestBody byte[] userDetails) throws InvalidProtocolBufferException {
        UserProto.User user = UserProto.User.parseFrom(userDetails);
        System.out.println("received user : "+user);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,"application/x-protobuf")
                .body(user.toByteArray());
    }

    @GetMapping(produces = "application/x-protobuf")
    public ResponseEntity<byte[]> getUser()
    {
        UserProto.User userProto = UserProto.User.newBuilder()
                .setId(1)
                .setName("Sonal")
                .setEmail("Sonal.first@gmail.com")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,"application/x-protobuf")
                .body(userProto.toByteArray());
    }

}
