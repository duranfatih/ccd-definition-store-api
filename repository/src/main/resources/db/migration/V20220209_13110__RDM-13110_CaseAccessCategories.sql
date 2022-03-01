ALTER TABLE ONLY public.role_to_access_profiles DROP CONSTRAINT unique_role_name_case_type_id_role_to_access_profiles;

ALTER TABLE ONLY public.role_to_access_profiles ADD CONSTRAINT
unique_role_name_case_type_id_case_access_categories_role_to_access_profiles UNIQUE (role_name, case_type_id, case_access_categories);