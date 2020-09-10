CREATE OR REPLACE FUNCTION hamming_distance(hash1 bigint, hash2 bigint)
    RETURNS real
    LANGUAGE 'plpgsql'

AS $BODY$
declare sum_bits int;
    declare diff bit(64);

begin
    diff = hash1::bit(64) # hash2::bit(64);
    sum_bits = 0;
    while diff::bigint != 0 loop
            sum_bits = sum_bits + (diff & 1::bit(64))::bigint;
            diff = diff >> 1;
        end loop;
    return  1 - sum_bits/64.;
end;
$BODY$;


CREATE OR REPLACE FUNCTION matcher(hash_to_compare bigint)
    RETURNS real
    LANGUAGE 'plpgsql'
AS $BODY$
declare max_matcher real;

begin
    select max(hamming_distance(hash_to_compare, hash)) into max_matcher from image_persist;
    if max_matcher is null then  max_matcher = 0.; end if;
    return max_matcher;
end;
$BODY$;