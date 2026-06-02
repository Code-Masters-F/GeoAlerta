


SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


COMMENT ON SCHEMA "public" IS 'standard public schema';



CREATE EXTENSION IF NOT EXISTS "pg_stat_statements" WITH SCHEMA "extensions";






CREATE EXTENSION IF NOT EXISTS "pgcrypto" WITH SCHEMA "extensions";






CREATE EXTENSION IF NOT EXISTS "supabase_vault" WITH SCHEMA "vault";






CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA "extensions";





SET default_tablespace = '';

SET default_table_access_method = "heap";


CREATE TABLE IF NOT EXISTS "public"."alertas" (
    "id" integer NOT NULL,
    "nome" character varying(60) NOT NULL,
    "tipo" character varying(30) NOT NULL,
    "grau_gravidade" character varying(20) NOT NULL,
    "data_de_emissao" timestamp without time zone NOT NULL,
    "descricao" "text"
);


ALTER TABLE "public"."alertas" OWNER TO "postgres";


ALTER TABLE "public"."alertas" ALTER COLUMN "id" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME "public"."alertas_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);



CREATE TABLE IF NOT EXISTS "public"."empresaagricola" (
    "cnpj" character(14) NOT NULL,
    "nomefantasia" character varying(60) NOT NULL,
    "email" character varying(255) NOT NULL,
    "senha_hash" character varying(255) NOT NULL,
    CONSTRAINT "ck_empresa_cnpj_formato" CHECK (("cnpj" ~ '^[0-9]{14}$'::"text"))
);


ALTER TABLE "public"."empresaagricola" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."enderecos" (
    "id" integer NOT NULL,
    "cnpj" character(14) NOT NULL,
    "erd_pluscode" character varying(11) NOT NULL
);


ALTER TABLE "public"."enderecos" OWNER TO "postgres";


ALTER TABLE "public"."enderecos" ALTER COLUMN "id" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME "public"."enderecos_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);



CREATE TABLE IF NOT EXISTS "public"."notificacoesrecebidas" (
    "cnpj" character(14) NOT NULL,
    "alertas_id" integer NOT NULL
);


ALTER TABLE "public"."notificacoesrecebidas" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."regioesafetadas" (
    "id" integer NOT NULL,
    "alertas_id" integer NOT NULL,
    "erd_pluscode" character varying(11) NOT NULL
);


ALTER TABLE "public"."regioesafetadas" OWNER TO "postgres";


ALTER TABLE "public"."regioesafetadas" ALTER COLUMN "id" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME "public"."regioesafetadas_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);



ALTER TABLE ONLY "public"."alertas"
    ADD CONSTRAINT "pk_alertas" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."empresaagricola"
    ADD CONSTRAINT "pk_empresa_agricola" PRIMARY KEY ("cnpj");



ALTER TABLE ONLY "public"."enderecos"
    ADD CONSTRAINT "pk_enderecos" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."notificacoesrecebidas"
    ADD CONSTRAINT "pk_notificacoes_recebidas" PRIMARY KEY ("cnpj", "alertas_id");



ALTER TABLE ONLY "public"."regioesafetadas"
    ADD CONSTRAINT "pk_regioes_afetadas" PRIMARY KEY ("id");



CREATE UNIQUE INDEX "uq_empresa_agricola_email_ci" ON "public"."empresaagricola" USING "btree" ("lower"(("email")::"text"));



ALTER TABLE ONLY "public"."enderecos"
    ADD CONSTRAINT "fk_enderecos_empresa" FOREIGN KEY ("cnpj") REFERENCES "public"."empresaagricola"("cnpj");



ALTER TABLE ONLY "public"."notificacoesrecebidas"
    ADD CONSTRAINT "fk_notificacoes_alerta" FOREIGN KEY ("alertas_id") REFERENCES "public"."alertas"("id");



ALTER TABLE ONLY "public"."notificacoesrecebidas"
    ADD CONSTRAINT "fk_notificacoes_empresa" FOREIGN KEY ("cnpj") REFERENCES "public"."empresaagricola"("cnpj");



ALTER TABLE ONLY "public"."regioesafetadas"
    ADD CONSTRAINT "fk_regioes_alerta" FOREIGN KEY ("alertas_id") REFERENCES "public"."alertas"("id");





ALTER PUBLICATION "supabase_realtime" OWNER TO "postgres";


GRANT USAGE ON SCHEMA "public" TO "postgres";
GRANT USAGE ON SCHEMA "public" TO "anon";
GRANT USAGE ON SCHEMA "public" TO "authenticated";
GRANT USAGE ON SCHEMA "public" TO "service_role";





































































































































































GRANT ALL ON TABLE "public"."alertas" TO "anon";
GRANT ALL ON TABLE "public"."alertas" TO "authenticated";
GRANT ALL ON TABLE "public"."alertas" TO "service_role";



GRANT ALL ON SEQUENCE "public"."alertas_id_seq" TO "anon";
GRANT ALL ON SEQUENCE "public"."alertas_id_seq" TO "authenticated";
GRANT ALL ON SEQUENCE "public"."alertas_id_seq" TO "service_role";



GRANT ALL ON TABLE "public"."empresaagricola" TO "anon";
GRANT ALL ON TABLE "public"."empresaagricola" TO "authenticated";
GRANT ALL ON TABLE "public"."empresaagricola" TO "service_role";



GRANT ALL ON TABLE "public"."enderecos" TO "anon";
GRANT ALL ON TABLE "public"."enderecos" TO "authenticated";
GRANT ALL ON TABLE "public"."enderecos" TO "service_role";



GRANT ALL ON SEQUENCE "public"."enderecos_id_seq" TO "anon";
GRANT ALL ON SEQUENCE "public"."enderecos_id_seq" TO "authenticated";
GRANT ALL ON SEQUENCE "public"."enderecos_id_seq" TO "service_role";



GRANT ALL ON TABLE "public"."notificacoesrecebidas" TO "anon";
GRANT ALL ON TABLE "public"."notificacoesrecebidas" TO "authenticated";
GRANT ALL ON TABLE "public"."notificacoesrecebidas" TO "service_role";



GRANT ALL ON TABLE "public"."regioesafetadas" TO "anon";
GRANT ALL ON TABLE "public"."regioesafetadas" TO "authenticated";
GRANT ALL ON TABLE "public"."regioesafetadas" TO "service_role";



GRANT ALL ON SEQUENCE "public"."regioesafetadas_id_seq" TO "anon";
GRANT ALL ON SEQUENCE "public"."regioesafetadas_id_seq" TO "authenticated";
GRANT ALL ON SEQUENCE "public"."regioesafetadas_id_seq" TO "service_role";









ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "service_role";






ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "service_role";






ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "service_role";































drop extension if exists "pg_net";


